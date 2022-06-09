package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.starter.cache.properties.CacheConfig;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存默认实现（caffeine cache）
 * @author lijunping on 2022/5/24
 */
public class CaffeineCache implements LocalCache{

    private Cache<Object, Object> cache;

    public CaffeineCache(CacheConfig cacheConfig) {
        this.cache = Caffeine.newBuilder()
                .initialCapacity(100_0)//初始大小
                .maximumSize(100_0)//最大数量
                .expireAfterWrite(100_0, TimeUnit.SECONDS)//过期时间
                .build();
    }

    @Override
    public Object get(Object key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.cleanUp();
    }
}
