package org.fuyi.wukong.core.datasource;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:17 pm
 * @since: 1.0
 **/
public class SimpleLayerDataSource implements LayerDataSource {

    private LayerDataSourceDriver driver;

    private Object identify;

    private String catalog;

    private String schema;

    private String table;

    /**
     * 常规定义缓存的key
     */
    private String commonDefinitionKey;

    /**
     * 字段定义缓存的key
     */
    private String fieldDefinitionKey;

    /**
     * 要素载体缓存的key
     */
    private String featureCarrierKey;

    public SimpleLayerDataSource() {
    }

    public SimpleLayerDataSource(LayerDataSourceDriver driver) {
        this.driver = driver;
    }

    public SimpleLayerDataSource(LayerDataSourceDriver driver, String catalog, String schema, String table) {
        this.driver = driver;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    public SimpleLayerDataSource(LayerDataSourceDriver driver, Object identify, String catalog, String schema, String table) {
        this.driver = driver;
        this.identify = identify;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    public SimpleLayerDataSource(LayerDataSourceDriver driver, Object identify, String catalog, String schema, String table, String commonDefinitionKey, String fieldDefinitionKey, String featureCarrierKey) {
        this.driver = driver;
        this.identify = identify;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.commonDefinitionKey = commonDefinitionKey;
        this.fieldDefinitionKey = fieldDefinitionKey;
        this.featureCarrierKey = featureCarrierKey;
    }

    @Override
    public LayerDataSourceDriver getDriver() {
        return driver;
    }

    @Override
    public void setDriver(LayerDataSourceDriver driver) {
        this.driver = driver;
    }

    @Override
    public Object getIdentify() {
        return identify;
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    @Override
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public String getCommonDefinitionKey() {
        return commonDefinitionKey;
    }

    @Override
    public void setCommonDefinitionKey(String commonDefinitionKey) {
        this.commonDefinitionKey = commonDefinitionKey;
    }

    @Override
    public String getFieldDefinitionKey() {
        return fieldDefinitionKey;
    }

    @Override
    public void setFieldDefinitionKey(String fieldDefinitionKey) {
        this.fieldDefinitionKey = fieldDefinitionKey;
    }

    @Override
    public String getFeatureCarrierKey() {
        return featureCarrierKey;
    }

    @Override
    public void setFeatureCarrierKey(String featureCarrierKey) {
        this.featureCarrierKey = featureCarrierKey;
    }

    @Override
    public String toString() {
        return "SimpleLayerDataSource{" +
                "driver=" + driver +
                ", identify=" + identify +
                ", catalog='" + catalog + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", commonDefinitionKey='" + commonDefinitionKey + '\'' +
                ", fieldDefinitionKey='" + fieldDefinitionKey + '\'' +
                ", featureCarrierKey='" + featureCarrierKey + '\'' +
                '}';
    }
}
