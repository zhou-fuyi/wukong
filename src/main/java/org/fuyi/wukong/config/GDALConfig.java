package org.fuyi.wukong.config;

import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.springframework.context.annotation.Configuration;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2022/7/24 20:49
 * @since: 1.0
 **/
@Configuration
public class GDALConfig {

    static {
        // 注册驱动
        ogr.RegisterAll();
        // 支持中文路径
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        // 属性表字段支持中文
//        gdal.SetConfigOption("SHAPE_ENCODING", "UTF-8");
        gdal.SetConfigOption("DXF_FEATURE_LIMIT_PER_BLOCK", "-1");
        gdal.SetConfigOption("PGCLIENTENCODING", "LATIN1");
        gdal.SetConfigOption("DXF_ENCODING", "GBK");
//        gdal.SetConfigOption("DXF_ENCODING", "Unicode");// 数据正常提取，中文乱码
//        gdal.SetConfigOption("DXF_ENCODING", "UTF-8"); // 数据正常提取，中文乱码
        gdal.SetConfigOption("DXF_MERGE_BLOCK_GEOMETRIES", "FALSE");
        gdal.SetConfigOption("DXF_TRANSLATE_ESCAPE_SEQUENCES", "TRUE");
    }
}
