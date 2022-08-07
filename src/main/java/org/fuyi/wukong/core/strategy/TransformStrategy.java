package org.fuyi.wukong.core.strategy;

import org.fuyi.wukong.core.context.TransformRequestContext;

/**
 * 变换策略
 *
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2022/7/24 20:46
 * @since: 1.0
 **/
public interface TransformStrategy {

    boolean support(Object reference);

    void transform(TransformRequestContext context);

}