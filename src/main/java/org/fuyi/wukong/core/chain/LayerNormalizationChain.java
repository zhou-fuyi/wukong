package org.fuyi.wukong.core.chain;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 图层归一化处理
 * 用于将分散于多个图幅, 或多种空间参考下的空间数据归拢到一起
 * <p>
 * 可以通过数据逻辑位置的重写, 空间参考的转换等手段进行处理
 *
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 3:53 pm
 * @since: 1.0
 **/
public interface LayerNormalizationChain {

    void doNormalize() throws IOException, SQLException;

    void release();
}
