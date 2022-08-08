package org.fuyi.wukong.core.handler.merge;

import org.fuyi.wukong.core.chain.LayerMergeChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:11 pm
 * @since: 1.0
 **/
public abstract class AbstractLayerMergeHandler implements LayerMergeHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void merge(LayerTransformContext context, LayerDefinition definition, LayerMergeChain chain) throws IOException, SQLException {
        if (match(definition)) {
            try {
                preMerge(context, definition);
                doMerge(context, definition);
            } catch (Exception exception) {
                logger.error(exception.getMessage());
                exception.printStackTrace();

            } finally {
                postMerge(context, definition);
            }
        }
    }


    /**
     * 前置处理
     *
     * @param context
     * @param definition
     */
    protected void preMerge(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
    }


    /**
     * 命令处理
     *
     * @param context
     * @param definition
     */
    protected abstract void doMerge(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException;

    /**
     * 后置处理
     *
     * @param context
     * @param definition
     */
    protected void postMerge(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        context.pushHandler(this);
    }
}
