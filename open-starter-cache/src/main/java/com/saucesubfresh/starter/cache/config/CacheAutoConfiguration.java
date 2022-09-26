/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.cache.config;

import com.saucesubfresh.starter.cache.annotation.EnableOpenCache;
import com.saucesubfresh.starter.cache.aspect.CacheAspect;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.executor.CacheExecutorErrorHandler;
import com.saucesubfresh.starter.cache.executor.DefaultCacheExecutor;
import com.saucesubfresh.starter.cache.executor.DefaultCacheExecutorErrorHandler;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.factory.DefaultConfigFactory;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.generator.SimpleKeyGenerator;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.manager.RedissonCaffeineCacheManager;
import com.saucesubfresh.starter.cache.message.CacheMessageListener;
import com.saucesubfresh.starter.cache.message.CacheMessageProducer;
import com.saucesubfresh.starter.cache.message.RedissonCacheMessageListener;
import com.saucesubfresh.starter.cache.message.RedissonCacheMessageProducer;
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
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnBean(annotation = {EnableOpenCache.class})
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheManager cacheManager(CacheProperties properties,
                                     ConfigFactory configFactory,
                                     RedissonClient redissonClient){
        return new RedissonCaffeineCacheManager(properties, configFactory, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheProcessor cacheProcessor(CacheManager cacheManager,
                                         CacheMessageProducer cacheMessageProducer){
        return new DefaultCacheProcessor(cacheManager, cacheMessageProducer);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheExecutorErrorHandler cacheExecutorFailureHandler(){
        return new DefaultCacheExecutorErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheExecutor cacheExecutor(CacheManager cacheManager,
                                       CacheExecutorErrorHandler executorErrorHandler){
        return new DefaultCacheExecutor(cacheManager, executorErrorHandler);
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
    public CacheMetricsScheduler cacheMetricsTrigger(CacheProperties properties,
                                                     CacheMetricsCollector cacheMetricsCollector,
                                                     CacheMetricsPusher cacheMetricsPusher){
        return new DefaultCacheMetricsScheduler(cacheMetricsPusher, properties, cacheMetricsCollector);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheMessageProducer cacheMessageProducer(CacheProperties properties,
                                                     RedissonClient redissonClient){
        return new RedissonCacheMessageProducer(redissonClient, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheMessageListener cacheMessageListener(CacheExecutor cacheExecutor,
                                                     CacheProperties properties,
                                                     RedissonClient redissonClient){
        return new RedissonCacheMessageListener(cacheExecutor, redissonClient, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheAspect cacheAspect(KeyGenerator keyGenerator, CacheProcessor cacheProcessor){
        return new CacheAspect(keyGenerator, cacheProcessor);
    }

}
