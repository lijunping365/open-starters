package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.exception.CacheException;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheMessage;

/**
 * @author lijunping on 2022/6/16
 */
public abstract class AbstractCacheExecutor implements CacheExecutor {

    private final CacheManager cacheManager;
    private final CacheExecutorErrorHandler errorHandler;

    protected AbstractCacheExecutor(CacheManager cacheManager, CacheExecutorErrorHandler errorHandler) {
        this.cacheManager = cacheManager;
        this.errorHandler = errorHandler;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }

    protected void onError(String errMsg, CacheMessage message){
        CacheException cacheException = new CacheException(errMsg, message);
        errorHandler.onExecuteError(cacheException);
    }
}
