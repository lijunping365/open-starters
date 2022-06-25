package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.RedisCaffeineCache;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-18 23:13
 */
public class RedisCaffeineCacheManager extends AbstractCacheManager {

    private static final String SAM = ":";
    private final CacheProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCaffeineCacheManager(CacheProperties properties, ConfigFactory configFactory, RedisTemplate<String, Object> redisTemplate) {
        super(configFactory);
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        cacheName = generate(namespace, cacheName);
        return new RedisCaffeineCache(cacheName, redisTemplate);
    }

    protected String generate(String namespace, String cacheName){
        return namespace + SAM + cacheName;
    }

}
