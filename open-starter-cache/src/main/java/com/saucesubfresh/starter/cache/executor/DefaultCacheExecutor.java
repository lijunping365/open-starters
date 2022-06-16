package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author lijunping on 2022/6/16
 */
public class DefaultCacheExecutor extends AbstractCacheExecutor {

    private final CacheManager cacheManager;

    public DefaultCacheExecutor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public boolean preloadCache(String cacheName, String cacheKey) {
        return false;
    }

    @Override
    public boolean clearCache(String cacheName) {
        return false;
    }

    @Override
    public boolean evictCache(String cacheName, String cacheKey) {
        return false;
    }
}
