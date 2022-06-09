package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.properties.CacheConfig;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

/**
 * 集群缓存默认实现（redisson cache）
 * @author lijunping on 2022/5/24
 */
public class RedissonCache implements RemoteCache{

    private RMapCache<Object, Object> mapCache;
    private final RedissonClient redissonClient;

    public RedissonCache(CacheConfig cacheConfig, RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}
