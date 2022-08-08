package org.fuyi.wukong.core.chain;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.capture.LayerTransformCaptureHandler;
import org.fuyi.wukong.core.handler.merge.LayerMergeHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:09 pm
 * @since: 1.0
 **/
public class DefaultLayerMergeChain implements LayerMergeChain{

    private List<LayerMergeHandler> handlers = Collections.EMPTY_LIST;

    private LayerDefinition definition;

    private LayerTransformCaptureHandler captureHandler;

    private LayerTransformContext context;

    private int pos = 0;


    DefaultLayerMergeChain() {
    }

    DefaultLayerMergeChain(List<LayerMergeHandler> handlers, LayerDefinition definition, LayerTransformCaptureHandler captureHandler, LayerTransformContext context) {
        this.handlers = handlers;
        this.definition = definition;
        this.captureHandler = captureHandler;
        this.context = context;
    }

    @Override
    public void doMerge() throws IOException, SQLException {
        if (Objects.isNull(context) || Objects.isNull(captureHandler)) {
            throw new IllegalArgumentException("context or captureHandler can not be null.");
        }
        if (pos == handlers.size()) {
            // 目标
            captureHandler.execute(context);
        } else {
            ++pos;
            LayerMergeHandler handler = handlers.get(pos - 1);
            handler.merge(context, definition, this);
        }
    }

    @Override
    public void release() {
        pos = 0;
    }
}
