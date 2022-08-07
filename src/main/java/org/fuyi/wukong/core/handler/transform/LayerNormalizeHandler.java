package org.fuyi.wukong.core.handler.transform;

import org.fuyi.wukong.core.Priority;
import org.fuyi.wukong.core.chain.LayerNormalizationChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 3:58 pm
 * @since: 1.0
 **/
public interface LayerNormalizeHandler extends Priority {

    void normalize(LayerTransformContext context, LayerDefinition definition, LayerNormalizationChain chain) throws IOException, SQLException;

    boolean match(LayerDefinition definition);
}
