package org.fuyi.wukong.core.handler.normalization;

import org.fuyi.wukong.core.chain.LayerNormalizationChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.TransformHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 3:58 pm
 * @since: 1.0
 **/
public interface LayerNormalizeHandler extends TransformHandler {

    void normalize(LayerTransformContext context, LayerDefinition definition, LayerNormalizationChain chain) throws IOException, SQLException;

}
