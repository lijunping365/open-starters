package com.saucesubfresh.starter.cache.api;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-06-08 23:39
 */
public class CacheServiceProvider implements CacheService{
    @Override
    public List<String> getCacheNames(String namespace) {
        return null;
    }

    @Override
    public boolean preheat(String namespace, String cacheName, String cacheKey) {
        return false;
    }

    @Override
    public boolean clearCache(String namespace, String cacheName) {
        return false;
    }

    @Override
    public boolean evictCache(String namespace, String cacheName, String cacheKey) {
        return false;
    }
}
