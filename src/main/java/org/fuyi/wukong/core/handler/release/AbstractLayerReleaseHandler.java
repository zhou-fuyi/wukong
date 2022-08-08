package org.fuyi.wukong.core.handler.release;

import org.fuyi.wukong.core.chain.LayerReleaseChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:39 pm
 * @since: 1.0
 **/
public abstract class AbstractLayerReleaseHandler implements LayerReleaseHandler{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void release(LayerTransformContext context, LayerDefinition definition, LayerReleaseChain chain) throws IOException, SQLException {
        if (match(definition)) {
            try {
                preRelease(context, definition);
                doRelease(context, definition);
            } catch (Exception exception) {
                logger.error(exception.getMessage());
                exception.printStackTrace();

            } finally {
                postRelease(context, definition);
            }
        }
    }



    /**
     * 前置处理
     *
     * @param context
     * @param definition
     */
    protected void preRelease(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
    }


    /**
     * 命令处理
     *
     * @param context
     * @param definition
     */
    protected abstract void doRelease(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException;

    /**
     * 后置处理
     *
     * @param context
     * @param definition
     */
    protected void postRelease(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        context.pushHandler(this);
    }
}
