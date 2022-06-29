package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.exception.CacheException;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping on 2022/6/16
 */
@Slf4j
public class DefaultCacheExecutor extends AbstractCacheExecutor {

    private final CacheExecutorErrorHandler failureHandler;

    public DefaultCacheExecutor(CacheManager cacheManager, CacheExecutorErrorHandler failureHandler) {
        super(cacheManager);
        this.failureHandler = failureHandler;
    }

    @Override
    public void preloadCache(String cacheName) throws CacheException {
        ClusterCache cache = super.getCache(cacheName);
        try {
            cache.preloadCache();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            onError(cacheName, e.getMessage());
        }
    }

    @Override
    public void clearCache(String cacheName) throws CacheException {
        ClusterCache cache = super.getCache(cacheName);
        try {
            cache.clear();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            onError(cacheName, e.getMessage());
        }
    }

    private void onError(String cacheName, String errMsg){
        CacheException cacheException = new CacheException(cacheName, errMsg);
        failureHandler.onExecuteError(cacheException);
    }
}
