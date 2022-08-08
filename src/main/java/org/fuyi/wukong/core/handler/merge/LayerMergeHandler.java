package org.fuyi.wukong.core.handler.merge;

import org.fuyi.wukong.core.chain.LayerMergeChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.TransformHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:10 pm
 * @since: 1.0
 **/
public interface LayerMergeHandler extends TransformHandler {

    void merge(LayerTransformContext context, LayerDefinition definition, LayerMergeChain chain) throws IOException, SQLException;

}
