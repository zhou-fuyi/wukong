package org.fuyi.wukong.core.strategy;

import org.fuyi.wukong.core.context.TransformRequestContext;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 7/8/2022 10:19 pm
 * @since: 1.0
 **/
public abstract class AbstractTransformStrategy implements TransformStrategy {

    @Override
    public void transform(TransformRequestContext context) {
        try {
            normalize(context);
            if (context.isMerge()) {
                merge(context);
            }
            release(context);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }

    protected abstract void normalize(TransformRequestContext context);

    protected abstract void merge(TransformRequestContext context);

    protected abstract void release(TransformRequestContext context);
}
