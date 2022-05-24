package com.saucesubfresh.starter.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.saucesubfresh.starter.cache.core.CaffeineCache;
import com.saucesubfresh.starter.cache.core.LocalCache;
import com.saucesubfresh.starter.cache.core.RedisCache;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lijunping on 2022/5/20
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(com.github.benmanes.caffeine.cache.Cache.class)
    public <K,V> LocalCache<K,V> localCache(Cache<K,V> cache){
        return new CaffeineCache<>(cache);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisTemplate.class)
    public <K,V> ClusterCache<K,V> remoteCache(RedisTemplate<K,V> redisTemplate){
        return new RedisCache<>(redisTemplate);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean(RedissonClient.class)
//    public
}
