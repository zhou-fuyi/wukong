package org.fuyi.wukong.core.handler;

import org.fuyi.wukong.core.Priority;
import org.fuyi.wukong.core.entity.LayerDefinition;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 8/8/2022 10:14 pm
 * @since: 1.0
 **/
public interface TransformHandler extends Priority {
    boolean match(LayerDefinition definition);
}
