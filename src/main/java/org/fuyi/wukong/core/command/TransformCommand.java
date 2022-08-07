package org.fuyi.wukong.core.command;

import org.fuyi.wukong.core.constant.TransformConstant;
import org.fuyi.wukong.core.entity.GridSet;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2/8/2022 10:51 pm
 * @since: 1.0
 **/
public class TransformCommand implements Serializable {

    private static final long serialVersionUID = 1442076756860989030L;
    /**
     * 格网对象
     */
    private GridSet gridSet;

    /**
     * 参考, 用于选择执行策略
     */
    private Object reference = TransformConstant.StrategyReference.GDAL;

    /**
     * 文件目录, 暂时不做过多的抽象
     */
    private String directory;

    private String suffix;

    private Map extras = Collections.EMPTY_MAP;

    public TransformCommand() {
    }

    public TransformCommand(String directory) {
        this.directory = directory;
    }

    public TransformCommand(String directory, Map extras) {
        this.directory = directory;
        this.extras = extras;
    }

    public TransformCommand(GridSet gridSet, Object reference, String directory, String suffix) {
        this.gridSet = gridSet;
        this.reference = reference;
        this.directory = directory;
        this.suffix = suffix;
    }

    public TransformCommand(GridSet gridSet, Object reference, String directory, String suffix, Map extras) {
        this.gridSet = gridSet;
        this.reference = reference;
        this.directory = directory;
        this.suffix = suffix;
        this.extras = extras;
    }

    public GridSet getGridSet() {
        return gridSet;
    }

    public void setGridSet(GridSet gridSet) {
        this.gridSet = gridSet;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Map getExtras() {
        return extras;
    }

    public void setExtras(Map extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "TransformCommand{" +
                "gridSet=" + gridSet +
                ", reference=" + reference +
                ", directory='" + directory + '\'' +
                ", suffix='" + suffix + '\'' +
                ", extras=" + extras +
                '}';
    }
}
