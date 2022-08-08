package org.fuyi.wukong.core.chain;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.capture.LayerNormalizationCaptureHandler;
import org.fuyi.wukong.core.handler.normalization.LayerNormalizeHandler;
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

    public static LayerNormalizationChain createLayerNormalizationChain(TransformRequestContext context, LayerDefinition definition) {
        List<LayerNormalizeHandler> layerNormalizeHandlers = obtainLayerNormalizationHandlers(context.getApplicationContext());
        LayerNormalizationCaptureHandler captureHandler = obtainLayerNormalizationCaptureHandler(context.getApplicationContext());
        LayerNormalizationChain normalizationChain = new DefaultLayerNormalizationChain(layerNormalizeHandlers, definition, captureHandler,
                new LayerTransformContext(context, definition));
        return normalizationChain;
    }

    protected static List<LayerNormalizeHandler> obtainLayerNormalizationHandlers(ApplicationContext context) {
        Map<String, LayerNormalizeHandler> layerNormalizeHandlerMap = context.getBeansOfType(LayerNormalizeHandler.class);
        if (CollectionUtils.isEmpty(layerNormalizeHandlerMap)) {
            throw new IllegalArgumentException("Could not get any instance of layer transform handler");
        }
        List<LayerNormalizeHandler> filters = layerNormalizeHandlerMap.values().stream().collect(Collectors.toList());
        // 正序
        Collections.sort(filters, Comparator.comparingInt(LayerNormalizeHandler::obtainPriority));
        return filters;
    }

    protected static LayerNormalizationCaptureHandler obtainLayerNormalizationCaptureHandler(ApplicationContext context) {
        LayerNormalizationCaptureHandler captureHandlerInstance = context.getBean(LayerNormalizationCaptureHandler.class);
        if (Objects.isNull(captureHandlerInstance)) {
            throw new IllegalArgumentException("Failed to get layer capture handler instance");
        }
        return captureHandlerInstance;
    }
}
