package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.saucesubfresh.starter.cache.properties.CacheProperties;

/**
 * 本地缓存默认实现（caffeine cache）
 * @author lijunping on 2022/5/24
 */
public class CaffeineCache<K, V> implements LocalCache<K, V>{

    private final CacheProperties cacheProperties;
    private final Cache<K, V> cache;

    public CaffeineCache(CacheProperties cacheProperties, Cache<K, V> cache) {
        this.cacheProperties = cacheProperties;
        this.cache = cache;
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

}