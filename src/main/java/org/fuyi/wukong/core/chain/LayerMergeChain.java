package org.fuyi.wukong.core.chain;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 9:47 pm
 * @since: 1.0
 **/
public interface LayerMergeChain {

    void doMerge() throws IOException, SQLException;

    void release();
}
