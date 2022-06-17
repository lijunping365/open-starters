package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author lijunping on 2022/6/16
 */
public abstract class AbstractCacheExecutor implements CacheExecutor {

    private final CacheManager cacheManager;

    protected AbstractCacheExecutor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }
}
