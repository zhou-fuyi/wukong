package org.fuyi.wukong.core;

import org.fuyi.wukong.core.command.TransformStatus;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 4:57 pm
 * @since: 1.0
 **/
public class DefaultLayerTransformResult<T> implements LayerTransformResult<T> {

    private TransformStatus status;

    private T body;

    private Map<Object, Object> extras;

    public DefaultLayerTransformResult(TransformStatus status) {
        this(null, null, status);
    }

    public DefaultLayerTransformResult(@Nullable T body, TransformStatus status) {
        this(body, null, status);
    }

    public DefaultLayerTransformResult(@Nullable T body, @Nullable Map<Object, Object> extras, TransformStatus status) {
        this.status = status;
        this.body = body;
        this.extras = extras;
    }

    @Override
    public TransformStatus status() {
        return status;
    }

    @Override
    public T getBody() {
        return body;
    }

    @Override
    public boolean hasBody() {
        return Objects.nonNull(body);
    }

    @Override
    public Map<Object, Object> extras() {
        return extras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultLayerTransformResult<?> that = (DefaultLayerTransformResult<?>) o;
        return status == that.status && Objects.equals(body, that.body) && Objects.equals(extras, that.extras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, body, extras);
    }

    public static LayerTransformResult Init(){
        return new DefaultLayerTransformResult(TransformStatus.INIT);
    }

    public static LayerTransformResult OK(){
        return new DefaultLayerTransformResult(TransformStatus.OK);
    }
}
