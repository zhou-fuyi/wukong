package org.fuyi.wukong.core.datasource;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:06 pm
 * @since: 1.0
 **/
public interface LayerDataSource {

    LayerDataSourceDriver getDriver();

    void setDriver(LayerDataSourceDriver driver);

    Object getIdentify();

    String getCatalog();

    void setCatalog(String catalog);

    String getSchema();

    void setSchema(String schema);

    String getTable();

    void setTable(String table);

    String getCommonDefinitionKey();

    void setCommonDefinitionKey(String commonDefinitionKey);

    String getFieldDefinitionKey();

    void setFieldDefinitionKey(String fieldDefinitionKey);

    String getFeatureCarrierKey();

    void setFeatureCarrierKey(String featureCarrierKey);
}
