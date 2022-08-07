package org.fuyi.wukong.core.handler.transform;

import org.fuyi.wukong.core.chain.LayerNormalizationChain;
import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 4:17 pm
 * @since: 1.0
 **/
public abstract class AbstractLayerNormalizeHandler implements LayerNormalizeHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void normalize(LayerTransformContext context, LayerDefinition definition, LayerNormalizationChain chain) throws IOException, SQLException {
        if (match(definition)) {
            try {
                preNormalize(context, definition);
                doNormalize(context, definition);
            } catch (Exception exception) {
                logger.error(exception.getMessage());
                exception.printStackTrace();

            } finally {
                postNormalize(context, definition);
            }
        }
    }


    /**
     * 前置处理
     *
     * @param context
     * @param definition
     */
    protected void preNormalize(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
    }


    /**
     * 命令处理
     *
     * @param context
     * @param definition
     */
    protected abstract void doNormalize(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException;

    /**
     * 后置处理
     *
     * @param context
     * @param definition
     */
    protected void postNormalize(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        context.pushHandler(this);
    }
}
