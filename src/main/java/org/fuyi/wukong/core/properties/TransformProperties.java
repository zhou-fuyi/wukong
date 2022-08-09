package org.fuyi.wukong.core.properties;

import org.fuyi.wukong.core.constant.TransformConstant;
import org.fuyi.wukong.core.entity.GridSet;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.fuyi.wukong.core.constant.TransformConstant.StrategyReference.MERGE_FLAG;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2/8/2022 10:59 pm
 * @since: 1.0
 **/
@ConfigurationProperties(prefix = "wu-kong.transform")
public class TransformProperties {

    private GridSet gridSet;

    private String reference = TransformConstant.StrategyReference.GDAL;

    private String suffix = TransformConstant.Suffix.ZIP;

    private String scale;

    private String sourceSpatialRef;

    private String sinkSpatialRef;

    private String storage;

    private String cachedPrefix = TransformConstant.Cache.PREFIX;

    private boolean merge = MERGE_FLAG;

    private LayerMergeHandlerProperties layerMerge;

    public TransformProperties() {
    }

    public GridSet getGridSet() {
        return gridSet;
    }

    public void setGridSet(GridSet gridSet) {
        this.gridSet = gridSet;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getSourceSpatialRef() {
        return sourceSpatialRef;
    }

    public void setSourceSpatialRef(String sourceSpatialRef) {
        this.sourceSpatialRef = sourceSpatialRef;
    }

    public String getSinkSpatialRef() {
        return sinkSpatialRef;
    }

    public void setSinkSpatialRef(String sinkSpatialRef) {
        this.sinkSpatialRef = sinkSpatialRef;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getCachedPrefix() {
        return cachedPrefix;
    }

    public void setCachedPrefix(String cachedPrefix) {
        this.cachedPrefix = cachedPrefix;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public LayerMergeHandlerProperties getLayerMerge() {
        return layerMerge;
    }

    public void setLayerMerge(LayerMergeHandlerProperties layerMerge) {
        this.layerMerge = layerMerge;
    }
}
