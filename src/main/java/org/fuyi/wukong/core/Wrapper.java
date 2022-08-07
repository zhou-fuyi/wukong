package org.fuyi.wukong.core;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:01 pm
 * @since: 1.0
 **/
public interface Wrapper {
    <T> T unwrap(Class<T> var1) throws IOException, SQLException;

    boolean isWrapperFor(Class<?> var1) throws IOException, SQLException;
}
