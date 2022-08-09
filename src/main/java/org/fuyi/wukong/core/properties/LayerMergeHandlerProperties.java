package org.fuyi.wukong.core.properties;

import java.util.Map;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 9/8/2022 9:58 pm
 * @since: 1.0
 **/
public class LayerMergeHandlerProperties {

    private Map<String, String> dbMapping;

    public LayerMergeHandlerProperties() {
    }

    public Map<String, String> getDbMapping() {
        return dbMapping;
    }

    public void setDbMapping(Map<String, String> dbMapping) {
        this.dbMapping = dbMapping;
    }

}
