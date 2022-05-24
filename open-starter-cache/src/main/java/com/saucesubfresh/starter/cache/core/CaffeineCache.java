package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * 本地缓存默认实现（caffeine cache）
 * @author lijunping on 2022/5/24
 */
public class CaffeineCache<K, V> implements LocalCache<K, V>{

    private final Cache<K, V> cache;

    public CaffeineCache(Cache<K, V> cache) {
        this.cache = cache;
    }
}
