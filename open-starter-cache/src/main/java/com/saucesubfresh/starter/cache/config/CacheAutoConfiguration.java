package com.saucesubfresh.starter.cache.config;

import com.saucesubfresh.starter.cache.aspect.CacheAspect;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.executor.DefaultCacheExecutor;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.generator.SimpleKeyGenerator;
import com.saucesubfresh.starter.cache.processor.CacheProcessor;
import com.saucesubfresh.starter.cache.processor.DefaultCacheProcessor;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.manager.DefaultCacheManager;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import com.saucesubfresh.starter.cache.service.CacheStatsService;
import com.saucesubfresh.starter.cache.service.CacheStatsServiceProvider;
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
    public CacheManager cacheManager(CacheProperties properties, RedissonClient redissonClient){
        return new DefaultCacheManager(properties, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheProcessor cacheHandler(CacheManager cacheManager){
        return new DefaultCacheProcessor(cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheExecutor cacheExecutor(CacheManager cacheManager){
        return new DefaultCacheExecutor(cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheStatsService cacheStatsService(CacheManager cacheManager){
        return new CacheStatsServiceProvider(cacheManager);
    }

}
