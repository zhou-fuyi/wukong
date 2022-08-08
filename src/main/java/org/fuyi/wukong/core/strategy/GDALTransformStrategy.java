package org.fuyi.wukong.core.strategy;

import org.fuyi.wukong.core.chain.LayerNormalizationChain;
import org.fuyi.wukong.core.chain.SimpleLayerNormalizeChainFactory;
import org.fuyi.wukong.core.constant.TransformConstant;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.datasource.SimpleLayerDataSource;
import org.fuyi.wukong.core.datasource.SimpleLayerDataSourceDriver;
import org.fuyi.wukong.core.entity.GridSet;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.fuyi.wukong.core.constant.TransformConstant.DataSource.GDAL_LAYER_DRIVER;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2022/7/24 20:48
 * @since: 1.0
 **/
@Component
public class GDALTransformStrategy extends AbstractTransformStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GDALTransformStrategy.class);

    @Override
    public boolean support(Object reference) {
        return Objects.nonNull(reference) && TransformConstant.StrategyReference.GDAL.equalsIgnoreCase(reference.toString());
    }

    /**
     * 归一化处理, 将分散的数据按图层进行集中, 目前采用Redis作为数据交换容器
     *
     * @param context
     */
    @Override
    protected void normalize(TransformRequestContext context) {
        GridSet gridSet = context.getGridSet();
        List<String> instances = gridSet.instances();
        if (CollectionUtils.isEmpty(instances)) {
            throw new IllegalArgumentException("Could not get a valid set of grid instances");
        }
        // 归一化处理, 将分散的数据按图层进行集中, 目前采用Redis作为数据交换容器
        instances.forEach(grid -> {
            File sourceFile = obtainFile(context.getDirectory(), grid, context.getSuffix());
            if (Objects.nonNull(sourceFile)) {
                DataSource source = ogr.Open(sourceFile.getAbsolutePath(), false);
                logger.info("dataset name is [{}]", source.getName());
                int layerCount = source.GetLayerCount();
                logger.info("The number of layers in the dataset is [{}]", layerCount);
                try {
                    for (int index = 0; index < layerCount; index++) {
                        Layer layer = source.GetLayer(index);
                        logger.info("layer name is [{}]", layer.GetName());
                        logger.info("feature count is [{}]", layer.GetFeatureCount());
                        LayerDefinition layerDefinition = buildLayerDefinition(context, layer, source.getName());
                        LayerNormalizationChain chain = SimpleLayerNormalizeChainFactory.createLayerTransformChain(context, layerDefinition);
                        try {
                            chain.doNormalize();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            chain.release();
                            if (Objects.nonNull(layer)) {
                                layer.delete();
                            }
                        }
                    }
                } finally {
                    if (Objects.nonNull(source)) {
                        source.delete();
                    }
                }
            }
        });
    }

    /**
     * 合并处理, 对归一化后的数据进行必要的合并, 是否合并则根据是否开启合并处理, 以及是否可以获取到对应图层的合并处理器
     * <p>
     * 需要使用数据库引擎, 便于进行数据检索, 默认将使用PostGIS
     *
     * @param context
     */
    @Override
    protected void merge(TransformRequestContext context) {

    }

    /**
     * 释放处理, 根据指令对完成前置处理的数据进行释放, 可以什么都不做, 也可以将其做全量导出或按需到处为常见格式(Shape File, GeoPackage, GeoJSON, WKT, PostgreSQL Dump), 或者发布为地图服务(GeoServer --> WMTS, MVT, WMS)
     *
     * @param context
     */
    @Override
    protected void release(TransformRequestContext context) {

    }

    /**
     * 根据条件获取到指定数据文件
     *
     * @param directory
     * @param instance
     * @param suffix
     * @return
     */
    protected File obtainFile(String directory, String instance, String suffix) {
        File file = null;
        if (directory.endsWith(File.separator)) {
            file = new File(directory + instance + suffix);
        } else {
            file = new File(directory + File.separator + instance + suffix);
        }
        if (!file.exists()) {
            logger.warn(String.format("file [%s] does not exist", file.getAbsolutePath()));
            return null;
        }
        return file;
    }

    /**
     * 构建{@link LayerDefinition}对象
     *
     * @param context
     * @param layer
     * @param origin
     * @return
     */
    protected LayerDefinition buildLayerDefinition(TransformRequestContext context, Layer layer, String origin) {
        SpatialReference spatialReference = layer.GetSpatialRef();
        String spatialRef = Objects.isNull(spatialReference) ? context.getSourceSpatialRef() : spatialReference.ExportToWkt();
        String layerName = layer.GetName();
        return new LayerDefinition(new SimpleLayerDataSource(new SimpleLayerDataSourceDriver(layer, GDAL_LAYER_DRIVER)), origin, context.getScale(), spatialRef, context.getSinkSpatialRef(), layerName.substring(0, 1), layerName, layerName, context.getStorage());
    }
}
