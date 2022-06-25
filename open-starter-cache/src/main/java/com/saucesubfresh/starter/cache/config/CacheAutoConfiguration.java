package com.saucesubfresh.starter.cache.config;

import com.saucesubfresh.starter.cache.annotation.EnableOpenCache;
import com.saucesubfresh.starter.cache.aspect.CacheAspect;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.executor.DefaultCacheExecutor;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.factory.DefaultConfigFactory;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.generator.SimpleKeyGenerator;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.manager.RedissonCaffeineCacheManager;
import com.saucesubfresh.starter.cache.metrics.*;
import com.saucesubfresh.starter.cache.processor.CacheProcessor;
import com.saucesubfresh.starter.cache.processor.DefaultCacheProcessor;
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
@ConditionalOnBean(annotation = {EnableOpenCache.class})
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheManager cacheManager(CacheProperties properties, ConfigFactory configFactory, RedissonClient redissonClient){
        return new RedissonCaffeineCacheManager(properties, configFactory, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheProcessor cacheProcessor(CacheManager cacheManager){
        return new DefaultCacheProcessor(cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheExecutor cacheExecutor(CacheManager cacheManager){
        return new DefaultCacheExecutor(cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigFactory configFactory(CacheProperties properties){
        return new DefaultConfigFactory(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheMetricsBuilder cacheMetricsBuilder(CacheProperties properties, CacheManager cacheManager){
        return new DefaultCacheMetricsBuilder(properties, cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheMetricsCollector cacheMetricsCollector(CacheManager cacheManager, CacheMetricsBuilder cacheMetricsBuilder){
        return new DefaultCacheMetricsCollector(cacheManager, cacheMetricsBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheMetricsPusher cacheMetricsPusher(){
        return new DefaultCacheMetricsPusher();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheMetricsTrigger cacheMetricsTrigger(CacheProperties properties, CacheMetricsCollector cacheMetricsCollector, CacheMetricsPusher cacheMetricsPusher){
        return new DefaultCacheMetricsTrigger(properties.getPeriod(), cacheMetricsPusher, cacheMetricsCollector);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheAspect cacheAspect(KeyGenerator keyGenerator, CacheProcessor cacheProcessor){
        return new CacheAspect(keyGenerator, cacheProcessor);
    }

}
