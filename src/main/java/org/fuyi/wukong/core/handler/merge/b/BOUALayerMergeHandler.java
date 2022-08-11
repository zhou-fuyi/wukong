package org.fuyi.wukong.core.handler.merge.b;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.datasource.LayerDataSource;
import org.fuyi.wukong.core.entity.FeatureCarrier;
import org.fuyi.wukong.core.entity.FieldDefinition;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.merge.AbstractLayerMergeHandler;
import org.fuyi.wukong.util.FeatureUtil;
import org.fuyi.wukong.util.StringUtil;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.fuyi.wukong.core.constant.FeatureClassificationConstant.B.ADMINISTRATIVE_REALM_AREA;
import static org.fuyi.wukong.core.constant.TransformConstant.Cache.LAYER_DEFINITION_HASH_SOURCE_SPATIAL_REF_KEY;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:16 pm
 * @since: 1.0
 **/
@Component
public class BOUALayerMergeHandler extends AbstractLayerMergeHandler {

    private static final String DEFAULT_DB_MAPPING_KEY = "default";

    private String dbMappingKey = DEFAULT_DB_MAPPING_KEY;

    private static final Vector LCO_OPTIONS = new Vector();

    private static int nGroupTransactions = 2000;

    private static final int OGRNullFID = -1;
    private static int nFIDToFetch = OGRNullFID;
    private static boolean bSkipFailures = true;

    static {
        LCO_OPTIONS.add("GEOMETRY_NAME=geom");
        LCO_OPTIONS.add("FID=gid");
        LCO_OPTIONS.add("PRECISION=NO");
        LCO_OPTIONS.add("OVERWRITE=YES");
    }

    public BOUALayerMergeHandler() {
    }

    @Override
    public boolean match(LayerDefinition definition) {
        return ADMINISTRATIVE_REALM_AREA.equalsIgnoreCase(definition.getLayerCode());
    }

    @Override
    protected void preMerge(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        /**
         * 将Redis中的数据写入PostGIS
         *
         * 图层命名, 临时图层: <code>definition.getFeatureCode()_definition.getLayerCode()_context.getRequestContext().getIdentify()</code>
         * 并更新LayerDataSource数据
         */
        String dbPath = obtainMergeDBMapping(context.getRequestContext());
        DataSource workspace = ogr.Open(dbPath, true);
        if (workspace == null) {
            throw new RuntimeException("[" + dbPath + "] 无法获取链接");
        }
        String layerName = getDefaultLayerName(context, definition);
        if (workspace.TestCapability(ogr.ODsCCreateLayer) == false) {
            logger.error(
                    "CreateLayer not supported by driver.");
            gdal.ErrorReset();
        }

        LayerDataSource dataSource = definition.getDataSource();
        RedisTemplate redisTemplate = dataSource.getDriver().unwrap(RedisTemplate.class);

        String commonDefinitionKey = dataSource.getCommonDefinitionKey();
        Object spatialRef = redisTemplate.opsForHash().get(commonDefinitionKey, LAYER_DEFINITION_HASH_SOURCE_SPATIAL_REF_KEY);
        Layer layer = workspace.CreateLayer(layerName);
        if (Objects.isNull(layer)) {
            layer = workspace.CreateLayer(layerName, new SpatialReference(spatialRef.toString()), ogr.wkbMultiPolygon, LCO_OPTIONS);
        }
        if (Objects.isNull(layer)) {
            logger.error("Layer {} creation failed.", layerName);
            throw new RuntimeException(String.format("Layer %s creation failed.", layerName));
        }
        String fieldDefinitionKey = dataSource.getFieldDefinitionKey();
        // 创建属性
        Set<FieldDefinition> fieldDefinitions = redisTemplate.opsForSet().members(fieldDefinitionKey);
        FieldDefinition[] fieldDefinitionArray = fieldDefinitions.toArray(new FieldDefinition[fieldDefinitions.size()]);
        for (int i = 0; i < fieldDefinitions.size(); i++) {
            FieldDefinition fieldDefinition = fieldDefinitionArray[i];
            FieldDefn fieldDefn = new FieldDefn(fieldDefinition.getName(), fieldDefinition.getType());
            fieldDefn.SetWidth(fieldDefinition.getWidth());
            fieldDefn.SetPrecision(fieldDefinition.getPrecision());
            layer.CreateField(fieldDefn);
        }

        /* -------------------------------------------------------------------- */
        /*      Transfer features.                                              */
        /* -------------------------------------------------------------------- */
        String featureCarrierKey = dataSource.getFeatureCarrierKey();
        List<FeatureCarrier> featureCarriers = redisTemplate.opsForList().range(featureCarrierKey, 0, -1);
        if (CollectionUtils.isEmpty(featureCarriers)) {
            logger.error("Could not get feature vector collection.");
            throw new RuntimeException("Could not get feature vector collection.");
        }
        int nFeaturesInTransaction = 0;
        int errorCount = 0;

        /* For datasources which support transactions, StartTransaction creates a transaction. */
        if (nGroupTransactions > 0) {
            layer.StartTransaction();
            layer.CommitTransaction();
            layer.StartTransaction();
        }
        int index = 0;
        FeatureDefn featureDefn = layer.GetLayerDefn();
        while (index < featureCarriers.size()) {
            FeatureCarrier carrier = featureCarriers.get(index);
            if (carrier == null)
                break;
            Feature feature = new Feature(featureDefn);
//            feature.SetFID(carrier.getFid());
            fillFields(feature, fieldDefinitions, carrier);
            // 为自定义字段赋值
            feature.SetGeometry(Geometry.CreateFromWkt(carrier.getGeometry()));
            int nParts = 0;
            int nIters = 1;
            for (int iPart = 0; iPart < nIters; iPart++) {
                if (++nFeaturesInTransaction == nGroupTransactions) {
                    layer.CommitTransaction();
                    layer.StartTransaction();
                    nFeaturesInTransaction = 0;
                }
                gdal.ErrorReset();
                int createFeatureResult = -1;
                try {
                    createFeatureResult = layer.CreateFeature(feature);
                } catch (Exception e) {
                    if (++errorCount == 1000) {
                        errorCount = 0;
                        e.printStackTrace();
                        logger.error("Layer.CreateFeature失败， layer`s GeometryType is [" + layer.GetGeomType()
                                + "] But the feature`s GeometryType is [" + feature.GetGeometryRef().GetGeometryType() + "]");
                    }
                }
                if (createFeatureResult != 0 && !bSkipFailures) {
                    if (nGroupTransactions > 0)
                        layer.RollbackTransaction();

                    feature.delete();
                    feature = null;
                    return;
                }

                feature.delete();
                feature = null;
            }
            index++;
        }
        if (nGroupTransactions > 0)
            layer.CommitTransaction();
        layer.delete();
        workspace.delete();
    }

    @Override
    protected void doMerge(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {

        /**
         * 1. 去除非中国范围内的境界数据
         *
         * 2. 根据行政编码进行空间数据合并(先合并插入, 后删除)
         */
        String dbPath = obtainMergeDBMapping(context.getRequestContext());
        DataSource workspace = ogr.Open(dbPath, true);
        if (workspace == null) {
            throw new RuntimeException("[" + dbPath + "] 无法获取链接");
        }
        String layerName = getDefaultLayerName(context, definition);
        Layer layer = workspace.GetLayer(layerName);
        if (layer == null) {
            throw new RuntimeException(String.format("Unable to get layer [%s]", layerName));
        }
        // 剔除境外境区
        workspace.ExecuteSQL(String.format("delete from %s where pac < %d or pac = %d", layerName, 100000, 250100));

        // 挑选出name重复的数据
        String nameDuplicateFilterSql = String.format("select a.* from %s a, (select name, count(*) _count from %s group by name) b where b._count > 1 and a.name = b.name", layerName, layerName);
        Layer nameDuplicateDataLayer = workspace.ExecuteSQL(nameDuplicateFilterSql);
        if (nameDuplicateDataLayer.GetFeatureCount() > 0) {
            Map<String, Feature> featureMap = new HashMap<>();
            Feature feature = null;
            while ((feature = nameDuplicateDataLayer.GetNextFeature()) != null) {
                String name = feature.GetFieldAsString("name");
                if (!StringUtils.hasText(name)){
                    logger.warn("name is empty");
                }
                if (featureMap.containsKey(name)) {
                    Feature cachedFeature = featureMap.get(name);
                    if (Objects.nonNull(feature.GetGeometryRef())) {

                        Geometry geometry = feature.GetGeometryRef();
                        int count = geometry.GetGeometryCount();
                        for (int index = 0; index < count; index++) {
                            cachedFeature.GetGeometryRef().AddGeometry(geometry.GetGeometryRef(index));
                        }
                    }
                } else {
                    featureMap.put(name, feature.Clone());
                }
                layer.DeleteFeature(feature.GetFID());
                feature.delete();
            }
            nameDuplicateDataLayer.delete();
            featureMap.values().forEach(other -> {
                other.GetGeometryRef().UnionCascaded();
            });
            if (nGroupTransactions > 0) {
                layer.StartTransaction();
                layer.CommitTransaction();
                layer.StartTransaction();
            }
            int nFeaturesInTransaction = 0;
            List<Feature> features = featureMap.values().stream().collect(Collectors.toList());
            for (int i = 0; i < featureMap.values().size(); i++) {
                if (++nFeaturesInTransaction == nGroupTransactions) {
                    layer.CommitTransaction();
                    layer.StartTransaction();
                    nFeaturesInTransaction = 0;
                }
                Feature restoredFeature = features.get(i);
                layer.CreateFeature(restoredFeature);
                restoredFeature.delete();
            }
            if (nGroupTransactions > 0)
                layer.CommitTransaction();
        }

        // 挑选出pac重复的数据
        String duplicateFilterSql = String.format("select a.* from %s a, (select pac, count(*) _count from %s group by pac) b where b._count > 1 and a.pac = b.pac", layerName, layerName);
        Layer duplicateDataLayer = workspace.ExecuteSQL(duplicateFilterSql);
        if (duplicateDataLayer.GetFeatureCount() > 0) {
            Map<Integer, Feature> featureMap = new HashMap<>();
            Feature feature = null;
            while ((feature = duplicateDataLayer.GetNextFeature()) != null) {
                Integer pac = feature.GetFieldAsInteger("pac");
                if (featureMap.containsKey(pac)) {
                    Feature cachedFeature = featureMap.get(pac);
                    if (Objects.nonNull(feature.GetGeometryRef())) {
                        Geometry geometry = feature.GetGeometryRef();
                        int count = geometry.GetGeometryCount();
                        for (int index = 0; index < count; index++) {
                            cachedFeature.GetGeometryRef().AddGeometry(geometry.GetGeometryRef(index));
                        }
                    }
                } else {
                    featureMap.put(pac, feature.Clone());
                }
                layer.DeleteFeature(feature.GetFID());
                feature.delete();
            }
            duplicateDataLayer.delete();
            featureMap.values().forEach(other -> {
                other.GetGeometryRef().UnionCascaded();
            });
            if (nGroupTransactions > 0) {
                layer.StartTransaction();
                layer.CommitTransaction();
                layer.StartTransaction();
            }
            int nFeaturesInTransaction = 0;
            List<Feature> features = featureMap.values().stream().collect(Collectors.toList());
            for (int i = 0; i < featureMap.values().size(); i++) {
                if (++nFeaturesInTransaction == nGroupTransactions) {
                    layer.CommitTransaction();
                    layer.StartTransaction();
                    nFeaturesInTransaction = 0;
                }
                Feature restoredFeature = features.get(i);
                layer.CreateFeature(restoredFeature);
                restoredFeature.delete();
            }
            if (nGroupTransactions > 0)
                layer.CommitTransaction();
        }

        layer.delete();
        workspace.delete();
    }

    protected String obtainMergeDBMapping(TransformRequestContext context) {
        String dbConfig = context.getTransformProperties().getLayerMerge().getDbMapping().get(dbMappingKey);
        if (!StringUtils.hasText(dbConfig)) {
            throw new IllegalArgumentException("Could not get database configuration");
        }
        return dbConfig;
    }

    protected void fillFields(Feature feature, Set<FieldDefinition> fieldDefinitions, FeatureCarrier carrier) {
        fieldDefinitions.forEach(fieldDefinition -> {
            if (fieldDefinition.getType() == 0) {
                feature.SetField(fieldDefinition.getName(), Integer.valueOf(carrier.getFields().get(fieldDefinition.getName()).toString()));
            } else if (fieldDefinition.getType() == 2) {
                feature.SetField(fieldDefinition.getName(), Double.valueOf(carrier.getFields().get(fieldDefinition.getName()).toString()));
            } else {
                feature.SetField(fieldDefinition.getName(), carrier.getFields().get(fieldDefinition.getName()).toString());
            }
        });
    }

    protected String getDefaultLayerName(LayerTransformContext context, LayerDefinition definition) {
        SpatialReference spatialReference = new SpatialReference(definition.getSourceSpatialRef());
        String layerName = definition.getFeatureCode() + "_" + definition.getLayerCode()
                + "_" + context.getRequestContext().getIdentify().toString().replaceAll("-", "");
        if (Objects.nonNull(spatialReference)) {
            layerName = layerName + "_" + spatialReference.GetName();
        }
        layerName = StringUtil.replaceAllFirstNumber(layerName).toLowerCase(Locale.ROOT);
        return layerName;
    }
}
