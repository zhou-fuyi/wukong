package org.fuyi.wukong.core.handler.capture;

import org.fuyi.wukong.core.DefaultLayerTransformResult;
import org.fuyi.wukong.core.context.LayerTransformContext;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 9:56 pm
 * @since: 1.0
 **/
public class SimpleLayerTransformCaptureHandler implements LayerTransformCaptureHandler{
    @Override
    public void execute(LayerTransformContext context) {
        context.setResult(DefaultLayerTransformResult.OK());
    }
}
