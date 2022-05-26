package com.saucesubfresh.starter.cache.config;

import com.saucesubfresh.starter.cache.aspect.CacheAspect;
import com.saucesubfresh.starter.cache.core.CaffeineCache;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.LocalCache;
import com.saucesubfresh.starter.cache.core.RedisCache;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.generator.SimpleKeyGenerator;
import com.saucesubfresh.starter.cache.handler.CacheHandler;
import com.saucesubfresh.starter.cache.handler.DefaultCacheHandler;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
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
    public <K,V> LocalCache<K,V> localCache(CacheProperties cacheProperties){
        return new CaffeineCache<>(cacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public <K,V> ClusterCache<K,V> clusterCache(CacheProperties cacheProperties){
        return new RedisCache<>(cacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public <K,V> CacheHandler cacheHandler(KeyGenerator keyGenerator, LocalCache<K,V> localCache, ClusterCache<K,V> clusterCache){
        return new DefaultCacheHandler<>(keyGenerator, localCache, clusterCache);
    }
}
