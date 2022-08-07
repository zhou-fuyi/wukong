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

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2022/7/24 20:48
 * @since: 1.0
 **/
@Component
public class GDALTransformStrategy implements TransformStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GDALTransformStrategy.class);

    @Override
    public boolean support(Object reference) {
        return Objects.nonNull(reference) && TransformConstant.StrategyReference.GDAL.equalsIgnoreCase(reference.toString());
    }

    @Override
    public void transform(TransformRequestContext context) {
        GridSet gridSet = context.getGridSet();
        List<String> instances = gridSet.instances();
        if (CollectionUtils.isEmpty(instances)) {
            throw new IllegalArgumentException("Could not get a valid set of grid instances");
        }
        instances.forEach(grid -> {
            File sourceFile = obtainFile(context.getDirectory(), grid, context.getSuffix());
            if (Objects.nonNull(sourceFile)) {
                DataSource source = ogr.Open(sourceFile.getAbsolutePath(), false);
                logger.info("dataset name is [{}]", source.getName());
                int layerCount = source.GetLayerCount();
                logger.info("The number of layers in the dataset is [{}]", layerCount);
                try {
                    for (int index = 0; index < layerCount; index++) {
                        // FIXME: 5/8/2022 开始处理图层
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

    protected LayerDefinition buildLayerDefinition(TransformRequestContext context, Layer layer, String origin) {
        SpatialReference spatialReference = layer.GetSpatialRef();
        String spatialRef = Objects.isNull(spatialReference) ? context.getSourceSpatialRef() : spatialReference.ExportToWkt();
        String layerName = layer.GetName();
        return new LayerDefinition(new SimpleLayerDataSource(new SimpleLayerDataSourceDriver(layer)), origin, context.getScale(), spatialRef, context.getSinkSpatialRef(), layerName.substring(0, 1), layerName, layerName, context.getStorage());
    }
}
