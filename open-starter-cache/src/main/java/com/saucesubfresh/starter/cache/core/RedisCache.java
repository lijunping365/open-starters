package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 集群缓存默认实现（redis cache）
 * @author lijunping on 2022/5/24
 */
public class RedisCache<K, V> implements ClusterCache<K, V> {

    private final CacheProperties cacheProperties;
    private RedisTemplate<K, V> redisTemplate;

    public RedisCache(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void evict(K key) {

    }
}
