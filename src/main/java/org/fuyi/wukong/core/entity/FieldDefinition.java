package org.fuyi.wukong.core.entity;

import java.io.Serializable;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 7/8/2022 2:50 pm
 * @since: 1.0
 **/
public class FieldDefinition implements Serializable {

    private static final long serialVersionUID = -5869502021054691908L;
    /**
     * 属性名称
     */
    private String name;

    /**
     * 类型标识
     */
    private int type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 宽度
     */
    private int width;

    /**
     * 精度
     */
    private int precision;

    public FieldDefinition() {
    }

    public FieldDefinition(String name, int type, String typeName, int width, int precision) {
        this.name = name;
        this.type = type;
        this.typeName = typeName;
        this.width = width;
        this.precision = precision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public String toString() {
        return "FieldDefinition{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", width=" + width +
                ", precision=" + precision +
                '}';
    }
}
