package org.fuyi.wukong.core.chain;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.capture.LayerMergeCaptureHandler;
import org.fuyi.wukong.core.handler.merge.LayerMergeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:25 pm
 * @since: 1.0
 **/
public class SimpleLayerMergeChainFactory {

    private final static Logger logger = LoggerFactory.getLogger(SimpleLayerNormalizeChainFactory.class);

    private SimpleLayerMergeChainFactory() {
    }

    public static LayerMergeChain createLayerMergeChain(TransformRequestContext context, LayerDefinition definition) {
        List<LayerMergeHandler> layerMergeHandlers = obtainLayerMergeHandlers(context.getApplicationContext());
        LayerMergeCaptureHandler captureHandler = obtainLayerMergeCaptureHandler(context.getApplicationContext());
        LayerMergeChain mergeChain = new DefaultLayerMergeChain(layerMergeHandlers, definition, captureHandler,
                new LayerTransformContext(context, definition));
        return mergeChain;
    }

    protected static List<LayerMergeHandler> obtainLayerMergeHandlers(ApplicationContext context) {
        Map<String, LayerMergeHandler> layerMergeHandlerMap = context.getBeansOfType(LayerMergeHandler.class);
        if (CollectionUtils.isEmpty(layerMergeHandlerMap)) {
            throw new IllegalArgumentException("Could not get any instance of layer merge handler");
        }
        List<LayerMergeHandler> filters = layerMergeHandlerMap.values().stream().collect(Collectors.toList());
        // 正序
        Collections.sort(filters, Comparator.comparingInt(LayerMergeHandler::obtainPriority));
        return filters;
    }

    protected static LayerMergeCaptureHandler obtainLayerMergeCaptureHandler(ApplicationContext context) {
        LayerMergeCaptureHandler captureHandlerInstance = context.getBean(LayerMergeCaptureHandler.class);
        if (Objects.isNull(captureHandlerInstance)) {
            throw new IllegalArgumentException("Failed to get layer capture handler instance");
        }
        return captureHandlerInstance;
    }
}
