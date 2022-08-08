package org.fuyi.wukong.core.context;

import org.fuyi.wukong.core.command.TransformCommand;
import org.fuyi.wukong.core.entity.GridSet;
import org.fuyi.wukong.core.entity.LayerDefinition;
import org.fuyi.wukong.core.properties.TransformProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 5/8/2022 9:59 pm
 * @since: 1.0
 **/
public class TransformRequestContext {

    private Object identify;
    private TransformProperties transformProperties;
    private TransformCommand transformCommand;

    private ApplicationContext applicationContext;

    private Map<Object, LayerDefinition> layerDefinitionMap = new ConcurrentHashMap<>();

    public TransformRequestContext(Object identify, TransformProperties transformProperties, TransformCommand transformCommand, ApplicationContext applicationContext) {
        this.identify = identify;
        this.transformProperties = transformProperties;
        this.transformCommand = transformCommand;
        this.applicationContext = applicationContext;
    }

    public GridSet getGridSet() {
        if (Objects.isNull(transformCommand.getGridSet())) {
            return transformProperties.getGridSet();
        }
        return transformCommand.getGridSet();
    }

    public Object getReference() {
        if (Objects.isNull(transformCommand.getReference())) {
            return transformProperties.getReference();
        }
        return transformCommand.getReference();
    }

    public String getSourceSpatialRef() {
        return transformProperties.getSourceSpatialRef();
    }

    public String getSinkSpatialRef() {
        return transformProperties.getSinkSpatialRef();
    }

    public String getDirectory() {
        return transformCommand.getDirectory();
    }

    public String getSuffix() {
        if (Objects.isNull(transformCommand.getSuffix())) {
            return transformProperties.getSuffix();
        }
        return transformCommand.getSuffix();
    }

    public String getScale() {
        return transformProperties.getScale();
    }

    public String getStorage() {
        return transformProperties.getStorage();
    }

    public Object getExt(Object key) {
        return transformCommand.getExtras().get(key);
    }

    public Map getExtras() {
        return transformCommand.getExtras();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getCachedPrefix() {
        return transformProperties.getCachedPrefix();
    }

    public boolean isMerge() {
        if (Objects.isNull(transformCommand.getMerge())) {
            return transformProperties.isMerge();
        }
        return transformCommand.getMerge();
    }

    public Object getIdentify() {
        return identify;
    }

    public void putLayerDefinition(LayerDefinition definition) {
        if (!layerDefinitionMap.containsKey(definition.getLayerCode())) {
            layerDefinitionMap.remove(definition.getLayerCode());
        }
        layerDefinitionMap.put(definition.getLayerCode(), definition);
    }

    public List<LayerDefinition> getLayerDefinitions() {
        if (CollectionUtils.isEmpty(layerDefinitionMap)) {
            return Collections.EMPTY_LIST;
        }
        return layerDefinitionMap.values().stream().collect(Collectors.toList());
    }
}
