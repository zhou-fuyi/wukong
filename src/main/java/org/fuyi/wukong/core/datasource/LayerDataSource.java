package org.fuyi.wukong.core.datasource;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:06 pm
 * @since: 1.0
 **/
public interface LayerDataSource {

    LayerDataSourceDriver getDriver();

    Object getIdentify();

    String getCatalog();

    String getSchema();

    String getTable();
}
