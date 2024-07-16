/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.cache.config;

import com.openbytecode.starter.cache.annotation.EnableOpenCache;
import com.openbytecode.starter.cache.aspect.CacheAspect;
import com.openbytecode.starter.cache.executor.CacheExecutor;
import com.openbytecode.starter.cache.handler.CacheListenerErrorHandler;
import com.openbytecode.starter.cache.executor.DefaultCacheExecutor;
import com.openbytecode.starter.cache.handler.CacheProducerErrorHandler;
import com.openbytecode.starter.cache.handler.DefaultCacheListenerErrorHandler;
import com.openbytecode.starter.cache.factory.ConfigFactory;
import com.openbytecode.starter.cache.factory.DefaultConfigFactory;
import com.openbytecode.starter.cache.generator.KeyGenerator;
import com.openbytecode.starter.cache.generator.SimpleKeyGenerator;
import com.openbytecode.starter.cache.handler.DefaultCacheProducerErrorHandler;
import com.openbytecode.starter.cache.manager.CacheManager;
import com.openbytecode.starter.cache.manager.RedissonCaffeineCacheManager;
import com.openbytecode.starter.cache.message.CacheMessageListener;
import com.openbytecode.starter.cache.message.CacheMessageProducer;
import com.openbytecode.starter.cache.message.RedissonCacheMessageListener;
import com.openbytecode.starter.cache.message.RedissonCacheMessageProducer;
import com.openbytecode.starter.cache.metrics.*;
import com.openbytecode.starter.cache.processor.CacheProcessor;
import com.openbytecode.starter.cache.processor.DefaultCacheProcessor;
import com.openbytecode.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping
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
    public CacheListenerErrorHandler cacheExecutorFailureHandler(){
        return new DefaultCacheListenerErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheProducerErrorHandler cacheProducerErrorHandler(){
        return new DefaultCacheProducerErrorHandler();
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
    public CacheMetricsScheduler cacheMetricsTrigger(CacheProperties properties,
                                                     CacheMetricsCollector cacheMetricsCollector,
                                                     CacheMetricsPusher cacheMetricsPusher){
        return new DefaultCacheMetricsScheduler(cacheMetricsPusher, properties, cacheMetricsCollector);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheMessageProducer cacheMessageProducer(CacheProperties properties,
                                                     RedissonClient redissonClient,
                                                     CacheProducerErrorHandler errorHandler){
        return new RedissonCacheMessageProducer(redissonClient, properties, errorHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public CacheMessageListener cacheMessageListener(CacheExecutor cacheExecutor,
                                                     CacheProperties properties,
                                                     RedissonClient redissonClient,
                                                     CacheListenerErrorHandler errorHandler){
        return new RedissonCacheMessageListener(cacheExecutor, redissonClient, properties, errorHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheAspect cacheAspect(KeyGenerator keyGenerator, CacheProcessor cacheProcessor){
        return new CacheAspect(keyGenerator, cacheProcessor);
    }

}
