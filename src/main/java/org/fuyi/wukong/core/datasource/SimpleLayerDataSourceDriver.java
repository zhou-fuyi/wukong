package org.fuyi.wukong.core.datasource;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:15 pm
 * @since: 1.0
 **/
public class SimpleLayerDataSourceDriver implements LayerDataSourceDriver{

    private Object instance;

    public SimpleLayerDataSourceDriver(Object instance) {
        this.instance = instance;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws IOException, SQLException {
        if (iface.isInstance(instance)) {
            return (T) instance;
        } else {
            throw new RuntimeException("LayerDataSource of type [" + this.getClass().getName() + "] cannot be unwrapped as [" + iface.getName() + "]");
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws IOException, SQLException {
        return iface.isInstance(instance);
    }
}
