package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author lijunping on 2022/6/16
 */
public class DefaultCacheExecutor extends AbstractCacheExecutor {

    public DefaultCacheExecutor(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public void preloadCache(String cacheName) {
        ClusterCache cache = getCache(cacheName);
        //cache.get();
    }

    @Override
    public void clearCache(String cacheName) {
        ClusterCache cache = getCache(cacheName);
        cache.clear();
    }
}
