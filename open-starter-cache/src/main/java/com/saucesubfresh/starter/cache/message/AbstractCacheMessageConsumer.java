package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:38
 */
public abstract class AbstractCacheMessageConsumer implements CacheMessageConsumer{

    private final CacheExecutor cacheExecutor;

    protected AbstractCacheMessageConsumer(CacheExecutor cacheExecutor) {
        this.cacheExecutor = cacheExecutor;
    }

    @Override
    public void handlerMessage(CacheMessage message) {
        String cacheName = message.getCacheName();
        CacheMessageCommand command = message.getCommand();

    }
}
