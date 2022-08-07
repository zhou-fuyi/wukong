package org.fuyi.wukong.core.context;

import org.fuyi.wukong.core.LayerTransformResult;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.transform.LayerNormalizeHandler;

import java.util.*;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 10:08 pm
 * @since: 1.0
 **/
public class LayerTransformContext {

    private TransformRequestContext requestContext;

    private LayerDefinition definition;

    private LayerTransformResult result;

    private Set<String> handlerStack = new TreeSet<>();

    public LayerTransformContext() {
    }

    public LayerTransformContext(TransformRequestContext requestContext, LayerDefinition definition) {
        this.requestContext = requestContext;
        this.definition = definition;
    }

    public LayerTransformContext(TransformRequestContext requestContext, LayerDefinition definition, LayerTransformResult result) {
        this.requestContext = requestContext;
        this.definition = definition;
        this.result = result;
    }

    public TransformRequestContext getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(TransformRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public LayerDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(LayerDefinition definition) {
        this.definition = definition;
    }

    public LayerTransformResult getResult() {
        return result;
    }

    public void setResult(LayerTransformResult result) {
        this.result = result;
    }

    public void pushHandler(LayerNormalizeHandler handler) {
        handlerStack.add(handler.getClass().getSimpleName());
    }
}
