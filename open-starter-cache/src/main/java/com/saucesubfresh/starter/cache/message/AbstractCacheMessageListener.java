package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:38
 */
public abstract class AbstractCacheMessageListener implements CacheMessageListener{

    private final CacheExecutor cacheExecutor;

    protected AbstractCacheMessageListener(CacheExecutor cacheExecutor) {
        this.cacheExecutor = cacheExecutor;
    }

    @Override
    public void onMessage(CacheMessage message) {
        cacheExecutor.execute(message);
    }
}
