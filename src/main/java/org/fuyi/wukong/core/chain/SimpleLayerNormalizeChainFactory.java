package org.fuyi.wukong.core.chain;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.transform.LayerNormalizeHandler;
import org.fuyi.wukong.core.handler.capture.LayerTransformCaptureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 6:42 pm
 * @since: 1.0
 **/
public final class SimpleLayerNormalizeChainFactory {

    private final static Logger logger = LoggerFactory.getLogger(SimpleLayerNormalizeChainFactory.class);

    private SimpleLayerNormalizeChainFactory() {
    }

    public static LayerNormalizationChain createLayerTransformChain(TransformRequestContext context, LayerDefinition definition) {
        List<LayerNormalizeHandler> layerNormalizeHandlers = obtainLayerTransformHandlers(context.getApplicationContext());
        LayerTransformCaptureHandler captureHandler = obtainLayerTransformCaptureHandler(context.getApplicationContext());
        LayerNormalizationChain transformChain = new DefaultLayerNormalizationChain(layerNormalizeHandlers, definition, captureHandler,
                new LayerTransformContext(context, definition));
        return transformChain;
    }

    protected static List<LayerNormalizeHandler> obtainLayerTransformHandlers(ApplicationContext context) {
        Map<String, LayerNormalizeHandler> layerTransformHandlerMap = context.getBeansOfType(LayerNormalizeHandler.class);
        if (CollectionUtils.isEmpty(layerTransformHandlerMap)) {
            throw new IllegalArgumentException("Could not get any instance of layer transform handler");
        }
        List<LayerNormalizeHandler> filters = layerTransformHandlerMap.values().stream().collect(Collectors.toList());
        // 正序
        Collections.sort(filters, Comparator.comparingInt(LayerNormalizeHandler::obtainPriority));
        return filters;
    }

    protected static LayerTransformCaptureHandler obtainLayerTransformCaptureHandler(ApplicationContext context) {
        LayerTransformCaptureHandler captureHandlerInstance = context.getBean(LayerTransformCaptureHandler.class);
        if (Objects.isNull(captureHandlerInstance)) {
            throw new IllegalArgumentException("Failed to get layer capture handler instance");
        }
        return captureHandlerInstance;
    }
}
