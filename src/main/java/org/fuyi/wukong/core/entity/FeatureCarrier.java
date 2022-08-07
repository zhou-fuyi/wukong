package org.fuyi.wukong.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Feature 载体
 *
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 7/8/2022 3:00 pm
 * @since: 1.0
 **/
public class FeatureCarrier implements Serializable {

    private static final long serialVersionUID = -892334305848200994L;
    private long fid;

    private String geometry;

    private int geometryType;

    private String geometryName;

    private Map fields = new HashMap();

    public FeatureCarrier() {
    }

    public FeatureCarrier(long fid, String geometry, int geometryType, String geometryName) {
        this.fid = fid;
        this.geometry = geometry;
        this.geometryType = geometryType;
        this.geometryName = geometryName;
    }

    public FeatureCarrier(long fid, String geometry, int geometryType, String geometryName, Map fields) {
        this.fid = fid;
        this.geometry = geometry;
        this.geometryType = geometryType;
        this.geometryName = geometryName;
        this.fields = fields;
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public int getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(int geometryType) {
        this.geometryType = geometryType;
    }

    public Map getFields() {
        return fields;
    }

    public void setFields(Map fields) {
        this.fields = fields;
    }

    public void putField(Object name, Object value) {
        fields.put(name, value);
    }

    public void deleteField(Object name){
        fields.remove(name);
    }
}
