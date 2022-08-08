package org.fuyi.wukong.core.handler.transform.b;

import org.fuyi.wukong.core.constant.TransformConstant;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.datasource.LayerDataSource;
import org.fuyi.wukong.core.datasource.SimpleLayerDataSourceDriver;
import org.fuyi.wukong.core.entity.FeatureCarrier;
import org.fuyi.wukong.core.entity.FieldDefinition;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.transform.AbstractLayerNormalizeHandler;
import org.fuyi.wukong.util.KeyGenerator;
import org.gdal.ogr.*;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.fuyi.wukong.core.constant.FeatureClassificationConstant.B.ADMINISTRATIVE_REALM_AREA;
import static org.fuyi.wukong.core.constant.TransformConstant.Cache.*;
import static org.fuyi.wukong.core.constant.TransformConstant.DataSource.REDIS_DRIVER;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 10:23 pm
 * @since: 1.0
 **/
@Component
public class BOUALayerNormalizeHandler extends AbstractLayerNormalizeHandler {

    private RedisTemplate redisTemplate;

    public BOUALayerNormalizeHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void preNormalize(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        LayerDataSource dataSource = definition.getDataSource();
        Layer layer = dataSource.getDriver().unwrap(Layer.class);
        FeatureDefn featureDefn = layer.GetLayerDefn();
        int fieldCount = featureDefn.GetFieldCount();
        List<FieldDefinition> fieldDefinitions = new ArrayList<>(fieldCount);
        for (int index = 0; index < fieldCount; index++) {
            FieldDefn fieldDefn = featureDefn.GetFieldDefn(index);
            logger.info("field name is {}, typeName is {}, fieldType is {}, fieldTypeName is {}, width is {}, Precision is {}", fieldDefn.GetNameRef(),
                    fieldDefn.GetTypeName(), fieldDefn.GetFieldType(), fieldDefn.GetFieldTypeName(fieldDefn.GetFieldType()), fieldDefn.GetWidth(), fieldDefn.GetPrecision());
            fieldDefinitions.add(new FieldDefinition(fieldDefn.GetNameRef(), fieldDefn.GetFieldType(), fieldDefn.GetFieldTypeName(fieldDefn.GetFieldType()), fieldDefn.GetWidth(),
                    fieldDefn.GetPrecision()));
        }
        definition.setFieldDefinitions(fieldDefinitions);
        logger.info("layerDefinition is {}", definition);
        String commonDefinitionKey = KeyGenerator.cacheKeyGenerate(context.getRequestContext().getCachedPrefix(), NORMALIZATION_SEGMENT, context.getRequestContext().getIdentify().toString(),
                definition.getFeatureCode(), definition.getLayerCode(), LAYER_DEFINITION_KEY);
        if (!redisTemplate.hasKey(commonDefinitionKey)) {
            definition.getDataSource().setCommonDefinitionKey(commonDefinitionKey);
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(commonDefinitionKey);
            boundHashOperations.put(LAYER_DEFINITION_HASH_SCALE_KEY, definition.getScale());
            boundHashOperations.put(LAYER_DEFINITION_HASH_SOURCE_SPATIAL_REF_KEY, definition.getSourceSpatialRef());
            boundHashOperations.put(LAYER_DEFINITION_HASH_SINK_SPATIAL_REF_KEY, definition.getSinkSpatialRef());
            boundHashOperations.put(LAYER_DEFINITION_HASH_FEATURE_CODE_KEY, definition.getFeatureCode());
            boundHashOperations.put(LAYER_DEFINITION_HASH_LAYER_CODE_KEY, definition.getLayerCode());
            boundHashOperations.put(LAYER_DEFINITION_HASH_RELEASE_KEY, definition.getRelease());
            boundHashOperations.expire(TransformConstant.Cache.TIME_OUT, TimeUnit.MINUTES);
        }
        String fieldDefinitionPrefixKey = KeyGenerator.cacheKeyGenerate(commonDefinitionKey, LAYER_DEFINITION_FIELD_KEY);
        if (!redisTemplate.hasKey(fieldDefinitionPrefixKey)) {
            definition.getDataSource().setFieldDefinitionKey(fieldDefinitionPrefixKey);
            BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(fieldDefinitionPrefixKey);
            fieldDefinitions.forEach(fieldDefinition -> boundSetOperations.add(fieldDefinition));
            boundSetOperations.expire(TransformConstant.Cache.TIME_OUT, TimeUnit.MINUTES);
        }
    }

    @Override
    protected void doNormalize(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        LayerDataSource dataSource = definition.getDataSource();
        Layer layer = dataSource.getDriver().unwrap(Layer.class);
        List<FeatureCarrier> featureCarriers = new ArrayList<>((int) layer.GetFeatureCount());
        String featureCachedKey = KeyGenerator.cacheKeyGenerate(context.getRequestContext().getCachedPrefix(), NORMALIZATION_SEGMENT, context.getRequestContext().getIdentify().toString(),
                definition.getFeatureCode(), definition.getLayerCode(), LAYER_FEATURE_KEY);
        definition.getDataSource().setFeatureCarrierKey(featureCachedKey);
        Feature feature = null;
        while ((feature = layer.GetNextFeature()) != null) {
            Geometry geometry = feature.GetGeometryRef();
            FeatureCarrier carrier = new FeatureCarrier(feature.GetFID(), geometry.ExportToWkt(), geometry.GetGeometryType(), geometry.GetGeometryName());
            fillFields(feature, definition, carrier);
            featureCarriers.add(carrier);
        }
        redisTemplate.opsForList().leftPushAll(featureCachedKey, featureCarriers);
        redisTemplate.expire(featureCachedKey, TransformConstant.Cache.TIME_OUT, TimeUnit.MINUTES);
    }

    @Override
    protected void postNormalize(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        definition.getDataSource().setDriver(new SimpleLayerDataSourceDriver(redisTemplate, REDIS_DRIVER));
    }

    @Override
    public boolean match(LayerDefinition definition) {
        return ADMINISTRATIVE_REALM_AREA.equalsIgnoreCase(definition.getLayerCode());
    }

    protected void fillFields(Feature feature, LayerDefinition definition, FeatureCarrier carrier) {
        definition.getFieldDefinitions().forEach(fieldDefinition -> {
            if (fieldDefinition.getType() == 0) {
                carrier.putField(fieldDefinition.getName(), feature.GetFieldAsInteger(fieldDefinition.getName()));
            } else if (fieldDefinition.getType() == 2) {
                carrier.putField(fieldDefinition.getName(), feature.GetFieldAsDouble(fieldDefinition.getName()));
            } else {
                carrier.putField(fieldDefinition.getName(), feature.GetFieldAsString(fieldDefinition.getName()));
            }
        });
    }
}
