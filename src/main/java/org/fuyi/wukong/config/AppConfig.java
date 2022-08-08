package org.fuyi.wukong.config;

import org.fuyi.wukong.core.WuKongTransformManager;
import org.fuyi.wukong.core.handler.capture.LayerNormalizationCaptureHandler;
import org.fuyi.wukong.core.properties.TransformProperties;
import org.fuyi.wukong.core.handler.capture.SimpleLayerNormalizationCaptureHandler;
import org.fuyi.wukong.core.strategy.TransformStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 5/8/2022 10:20 pm
 * @since: 1.0
 **/
@Configuration
@EnableConfigurationProperties(TransformProperties.class)
public class AppConfig {

    @Bean
    @ConditionalOnMissingBean(value = WuKongTransformManager.class)
    public WuKongTransformManager defaultWuKongStrategyManager(List<TransformStrategy> transformStrategies, TransformProperties transformProperties) {
        return new WuKongTransformManager(transformStrategies, transformProperties);
    }

    @Bean
    @ConditionalOnMissingBean(value = LayerNormalizationCaptureHandler.class)
    public LayerNormalizationCaptureHandler layerNormalizationCaptureHandler() {
        return new SimpleLayerNormalizationCaptureHandler();
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory){
        RedisTemplate redisTemplate =  new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        return redisTemplate;
    }
}
