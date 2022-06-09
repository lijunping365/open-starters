package com.saucesubfresh.starter.cache.config;

import com.saucesubfresh.starter.cache.aspect.CacheAspect;
import com.saucesubfresh.starter.cache.core.*;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.generator.SimpleKeyGenerator;
import com.saucesubfresh.starter.cache.handler.CacheAnnotationHandler;
import com.saucesubfresh.starter.cache.handler.DefaultCacheAnnotationHandler;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.manager.DefaultCacheManager;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping on 2022/5/20
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnBean(CacheAspect.class)
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheManager cacheManager(RedissonClient redissonClient){
        return new DefaultCacheManager(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheAnnotationHandler cacheAnnotationHandler(CacheProperties cacheProperties, KeyGenerator keyGenerator, CacheManager cacheManager){
        return new DefaultCacheAnnotationHandler(cacheProperties, keyGenerator, cacheManager);
    }


}
