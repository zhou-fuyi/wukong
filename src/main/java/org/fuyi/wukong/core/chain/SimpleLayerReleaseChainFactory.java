package org.fuyi.wukong.core.chain;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.capture.LayerNormalizationCaptureHandler;
import org.fuyi.wukong.core.handler.capture.LayerReleaseCaptureHandler;
import org.fuyi.wukong.core.handler.normalization.LayerNormalizeHandler;
import org.fuyi.wukong.core.handler.release.LayerReleaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:43 pm
 * @since: 1.0
 **/
public class SimpleLayerReleaseChainFactory {

    private final static Logger logger = LoggerFactory.getLogger(SimpleLayerNormalizeChainFactory.class);

    private SimpleLayerReleaseChainFactory() {
    }

    public static LayerReleaseChain createLayerReleaseChain(TransformRequestContext context, LayerDefinition definition) {
        List<LayerReleaseHandler> layerReleaseHandlers = obtainLayerReleaseHandlers(context.getApplicationContext());
        LayerReleaseCaptureHandler captureHandler = obtainLayerReleaseCaptureHandler(context.getApplicationContext());
        LayerReleaseChain releaseChain = new DefaultLayerReleaseChain(layerReleaseHandlers, definition, captureHandler,
                new LayerTransformContext(context, definition));
        return releaseChain;
    }

    protected static List<LayerReleaseHandler> obtainLayerReleaseHandlers(ApplicationContext context) {
        Map<String, LayerReleaseHandler> layerReleaseHandlerMap = context.getBeansOfType(LayerReleaseHandler.class);
        if (CollectionUtils.isEmpty(layerReleaseHandlerMap)) {
            throw new IllegalArgumentException("Could not get any instance of layer transform handler");
        }
        List<LayerReleaseHandler> filters = layerReleaseHandlerMap.values().stream().collect(Collectors.toList());
        // 正序
        Collections.sort(filters, Comparator.comparingInt(LayerReleaseHandler::obtainPriority));
        return filters;
    }

    protected static LayerReleaseCaptureHandler obtainLayerReleaseCaptureHandler(ApplicationContext context) {
        LayerReleaseCaptureHandler captureHandlerInstance = context.getBean(LayerReleaseCaptureHandler.class);
        if (Objects.isNull(captureHandlerInstance)) {
            throw new IllegalArgumentException("Failed to get layer capture handler instance");
        }
        return captureHandlerInstance;
    }
}
