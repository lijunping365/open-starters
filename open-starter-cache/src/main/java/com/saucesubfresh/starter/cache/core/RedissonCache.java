package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;

/**
 * 集群缓存默认实现（redisson cache）
 * @author lijunping on 2022/5/24
 */
public class RedissonCache<K, V> implements RemoteCache<K, V> {

    private final CacheProperties cacheProperties;
    private final RedissonClient redissonClient;

    public RedissonCache(CacheProperties cacheProperties, RedissonClient redissonClient) {
        this.cacheProperties = cacheProperties;
        this.redissonClient = redissonClient;
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
