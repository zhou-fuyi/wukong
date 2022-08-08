package org.fuyi.wukong.core.constant;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 5/8/2022 10:05 pm
 * @since: 1.0
 **/
public interface TransformConstant {

    interface StrategyReference {
        String GDAL = "gdal";
    }

    interface Suffix {
        String DIRECTORY = ".gdb";
        String ZIP = ".gdb.zip";
    }

    interface Cache {
        String PREFIX = "wukong";

        String DELIMITER = ":";

        int TIME_OUT = 60;

        String LAYER_DEFINITION_KEY = "definition";
        String LAYER_DEFINITION_FIELD_KEY = "field";
        String LAYER_FEATURE_KEY = "feature";

        String LAYER_DEFINITION_HASH_SCALE_KEY = "scale";
        String LAYER_DEFINITION_HASH_SOURCE_SPATIAL_REF_KEY = "sourceSpatialRef";
        String LAYER_DEFINITION_HASH_SINK_SPATIAL_REF_KEY = "sinkSpatialRef";
        String LAYER_DEFINITION_HASH_FEATURE_CODE_KEY = "featureCode";
        String LAYER_DEFINITION_HASH_LAYER_CODE_KEY = "layerCode";
        String LAYER_DEFINITION_HASH_RELEASE_KEY = "release";

        String NORMALIZATION_SEGMENT = "normalization";
        String MERGE_SEGMENT = "merge";
        String RELEASE_SEGMENT = "release";
    }

    interface DataSource {

        String GDAL_LAYER_DRIVER = "gdal_layer";

        String REDIS_DRIVER = "redis";

        String POSTGIS_DRIVER = "postgis";
    }

}
