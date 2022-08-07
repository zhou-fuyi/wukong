package org.fuyi.wukong.core;


import org.fuyi.wukong.core.command.TransformStatus;

import java.util.Map;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 4:52 pm
 * @since: 1.0
 **/
public interface LayerTransformResult<T> {

    TransformStatus status();

    T getBody();

    boolean hasBody();

    /**
     * 额外拓展功能，功能类似header
     * @return
     */
    Map<Object, Object> extras();
}
