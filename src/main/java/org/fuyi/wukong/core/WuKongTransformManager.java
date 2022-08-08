package org.fuyi.wukong.core;

import org.fuyi.wukong.core.command.TransformCommand;
import org.fuyi.wukong.core.context.TransformRequestContext;
import org.fuyi.wukong.core.properties.TransformProperties;
import org.fuyi.wukong.core.strategy.TransformStrategy;
import org.fuyi.wukong.util.CommonIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2022/7/24 20:45
 * @since: 1.0
 **/
public class WuKongTransformManager implements ApplicationContextAware, InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(WuKongTransformManager.class);
    private List<TransformStrategy> transformStrategies;
    private TransformProperties transformProperties;

    private ApplicationContext context;

    public WuKongTransformManager(List<TransformStrategy> transformStrategies, TransformProperties transformProperties) {
        this.transformStrategies = transformStrategies;
        this.transformProperties = transformProperties;
    }

    public void execute(TransformCommand command) {
        long startTime = System.currentTimeMillis();
        logger.info(String.format("command start at %d, with [%s]", startTime, command));
        TransformRequestContext transformRequestContext = buildTransformContext(command);
        TransformStrategy transformStrategy = obtainStrategy(transformRequestContext.getReference());
        if (Objects.isNull(transformStrategy)) {
            throw new IllegalArgumentException(String.format("Unable to get any support [%s] policy configuration", transformRequestContext.getReference()));
        }
        transformStrategy.transform(transformRequestContext);
        logger.info(String.format("command end at %d, with [%s]", System.currentTimeMillis() - startTime, command));
    }

    protected TransformStrategy obtainStrategy(Object reference) {
        for (int index = 0; index < transformStrategies.size(); index++) {
            if (transformStrategies.get(index).support(reference)) {
                return transformStrategies.get(index);
            }
        }
        throw new IllegalArgumentException(String.format("Unable to get any support [%s] policy configuration", reference));
    }

    protected TransformRequestContext buildTransformContext(TransformCommand command) {
        return new TransformRequestContext(CommonIDGenerator.next(), transformProperties, command, context);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(transformStrategies)) {
            throw new IllegalAccessException("Unable to get any policy configuration, please check if the [TransformStrategy] instance is created correctly");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
