package org.fuyi.wukong.core.datasource;

import java.util.Objects;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:17 pm
 * @since: 1.0
 **/
public class SimpleLayerDataSource implements LayerDataSource{

    private LayerDataSourceDriver driver;

    private Object identify;

    private String catalog;

    private String schema;

    private String table;

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

    @Override
    public LayerDataSourceDriver getDriver() {
        return driver;
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
    public String getSchema() {
        return schema;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleLayerDataSource that = (SimpleLayerDataSource) o;
        return Objects.equals(driver, that.driver) && Objects.equals(identify, that.identify) && Objects.equals(catalog, that.catalog) && Objects.equals(schema, that.schema) && Objects.equals(table, that.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driver, identify, catalog, schema, table);
    }

    @Override
    public String toString() {
        return "SimpleLayerDataSource{" +
                "driver=" + driver +
                ", identify=" + identify +
                ", catalog='" + catalog + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                '}';
    }
}
