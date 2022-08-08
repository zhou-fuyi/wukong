package org.fuyi.wukong.core.handler.release.b;

import org.fuyi.wukong.core.context.LayerTransformContext;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.handler.merge.AbstractLayerMergeHandler;
import org.fuyi.wukong.core.handler.release.AbstractLayerReleaseHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

import static org.fuyi.wukong.core.constant.FeatureClassificationConstant.B.ADMINISTRATIVE_REALM_AREA;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:16 pm
 * @since: 1.0
 **/
@Component
public class BOUALayerReleaseHandler extends AbstractLayerReleaseHandler {
    @Override
    public boolean match(LayerDefinition definition) {
        return ADMINISTRATIVE_REALM_AREA.equalsIgnoreCase(definition.getLayerCode());
    }

    @Override
    protected void doRelease(LayerTransformContext context, LayerDefinition definition) throws IOException, SQLException {
        logger.warn("release: do nothing here.");
    }
}
