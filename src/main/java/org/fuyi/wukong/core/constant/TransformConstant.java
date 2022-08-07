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
    }

}
