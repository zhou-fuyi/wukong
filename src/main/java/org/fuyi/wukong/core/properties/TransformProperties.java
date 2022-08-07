package org.fuyi.wukong.core.properties;

import org.fuyi.wukong.core.constant.TransformConstant;
import org.fuyi.wukong.core.entity.GridSet;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2/8/2022 10:59 pm
 * @since: 1.0
 **/
@ConfigurationProperties(prefix = "wukong.transform")
public class TransformProperties {

    private GridSet gridSet;

    private String reference = TransformConstant.StrategyReference.GDAL;

    private String suffix = TransformConstant.Suffix.ZIP;

    private String scale;

    private String sourceSpatialRef;

    private String sinkSpatialRef;

    private String storage;

    private String cachedPrefix = TransformConstant.Cache.PREFIX;

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
}
