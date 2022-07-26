package org.fuyi.wukong.core.chain;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.normalization.LayerNormalizeHandler;
import org.fuyi.wukong.core.handler.capture.LayerTransformCaptureHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 3:58 pm
 * @since: 1.0
 **/
public class DefaultLayerNormalizationChain implements LayerNormalizationChain {

    private List<LayerNormalizeHandler> handlers = Collections.EMPTY_LIST;

    private LayerDefinition definition;

    private LayerTransformCaptureHandler captureHandler;

    private LayerTransformContext context;

    private int pos = 0;

    DefaultLayerNormalizationChain() {
    }

    DefaultLayerNormalizationChain(List<LayerNormalizeHandler> handlers, LayerDefinition definition, LayerTransformCaptureHandler captureHandler, LayerTransformContext context) {
        this.handlers = handlers;
        this.definition = definition;
        this.captureHandler = captureHandler;
        this.context = context;
    }

    public List<LayerNormalizeHandler> getHandlers() {
        return handlers;
    }

    public LayerTransformContext getContext() {
        return context;
    }

    public LayerDefinition getDefinition() {
        return definition;
    }

    public LayerTransformCaptureHandler getCaptureHandler() {
        return captureHandler;
    }

    @Override
    public void doNormalize() throws IOException, SQLException {
        if (Objects.isNull(context) || Objects.isNull(captureHandler)) {
            throw new IllegalArgumentException("context or captureHandler can not be null.");
        }
        if (pos == handlers.size()) {
            // 目标
            captureHandler.execute(context);
        } else {
            ++pos;
            LayerNormalizeHandler handler = handlers.get(pos - 1);
            handler.normalize(context, definition, this);
        }
    }

    @Override
    public void release() {
        pos = 0;
    }
}
