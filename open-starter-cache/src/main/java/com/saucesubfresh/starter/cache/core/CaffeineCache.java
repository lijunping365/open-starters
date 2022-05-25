package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存默认实现（caffeine cache）
 * @author lijunping on 2022/5/24
 */
public class CaffeineCache<K, V> implements LocalCache<K, V>, InitializingBean {

    private final CacheProperties cacheProperties;
    private Cache<K, V> cache;

    public CaffeineCache(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void evict(K key) {
        cache.invalidate(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        builder.initialCapacity(100);
        builder.maximumSize(10_000);
        builder.softValues();
        builder.expireAfterAccess(1, TimeUnit.MINUTES);
        this.cache = builder.build();
    }
}
