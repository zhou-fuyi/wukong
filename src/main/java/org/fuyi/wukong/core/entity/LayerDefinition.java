package org.fuyi.wukong.core.entity;

import org.fuyi.wukong.core.datasource.LayerDataSource;

import java.io.Serializable;
import java.util.List;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2/8/2022 10:00 pm
 * @since: 1.0
 **/
public class LayerDefinition implements Serializable {

    private static final long serialVersionUID = -4617577002512068431L;

    private LayerDataSource dataSource;

    private String origin;

    /**
     * 比例尺（如：1000000，表示1:1000000）
     */
    private String scale;

    // FIXME: 2/8/2022 可以包装一下坐标系定义
    /**
     * 源坐标系（表现形式可为标准ID、PROJ Text、WKT Text）
     */
    private String sourceSpatialRef;

    /**
     * 目标坐标系（表现形式可为标准ID、PROJ Text、WKT Text）
     */
    private String sinkSpatialRef;

    /**
     * 要素分类码
     */
    private String featureCode;

    /**
     * 图层命名，也是图层分类码
     */
    private String name;

    /**
     * 图层分类码, 目前与图层分类码一致
     */
    private String layerCode;

    /**
     * 存储格式，是为自定义格式，暂时仅支持PostGIS；意为最终数据的存储格式
     */
    private String storage;

    private List<FieldDefinition> fieldDefinitions;

    public LayerDefinition() {
    }

    public LayerDefinition(LayerDataSource dataSource, String origin, String scale, String sourceSpatialRef, String sinkSpatialRef, String featureCode, String name, String layerCode, String storage) {
        this.dataSource = dataSource;
        this.origin = origin;
        this.scale = scale;
        this.sourceSpatialRef = sourceSpatialRef;
        this.sinkSpatialRef = sinkSpatialRef;
        this.featureCode = featureCode;
        this.name = name;
        this.layerCode = layerCode;
        this.storage = storage;
    }

    public LayerDefinition(LayerDataSource dataSource, String origin, String scale, String sourceSpatialRef, String sinkSpatialRef, String featureCode, String name, String layerCode, String storage, List<FieldDefinition> fieldDefinitions) {
        this.dataSource = dataSource;
        this.origin = origin;
        this.scale = scale;
        this.sourceSpatialRef = sourceSpatialRef;
        this.sinkSpatialRef = sinkSpatialRef;
        this.featureCode = featureCode;
        this.name = name;
        this.layerCode = layerCode;
        this.storage = storage;
        this.fieldDefinitions = fieldDefinitions;
    }

    public LayerDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(LayerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayerCode() {
        return layerCode;
    }

    public void setLayerCode(String layerCode) {
        this.layerCode = layerCode;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public List<FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    public void setFieldDefinitions(List<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
    }

    @Override
    public String toString() {
        return "LayerDefinition{" +
                "dataSource=" + dataSource +
                ", origin='" + origin + '\'' +
                ", scale='" + scale + '\'' +
                ", sourceSpatialRef='" + sourceSpatialRef + '\'' +
                ", sinkSpatialRef='" + sinkSpatialRef + '\'' +
                ", featureCode='" + featureCode + '\'' +
                ", name='" + name + '\'' +
                ", layerCode='" + layerCode + '\'' +
                ", storage='" + storage + '\'' +
                ", fieldDefinitions=" + fieldDefinitions +
                '}';
    }
}
