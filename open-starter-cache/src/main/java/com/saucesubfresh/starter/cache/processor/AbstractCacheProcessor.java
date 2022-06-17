package com.saucesubfresh.starter.cache.processor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author: 李俊平
 * @Date: 2022-06-16 23:26
 */
public abstract class AbstractCacheProcessor implements CacheProcessor {

    private final CacheManager cacheManager;

    public AbstractCacheProcessor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }
}
