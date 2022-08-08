package org.fuyi.wukong.core.handler.release;

import org.fuyi.wukong.core.chain.LayerReleaseChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.TransformHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:36 pm
 * @since: 1.0
 **/
public interface LayerReleaseHandler extends TransformHandler {
    void release(LayerTransformContext context, LayerDefinition definition, LayerReleaseChain chain) throws IOException, SQLException;
}
