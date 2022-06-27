package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.manager.CacheManager;

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
        String cacheName = message.getCacheName();
        Object key = message.getKey();
        Object value = message.getValue();
        CacheMessageCommand command = message.getCommand();
        switch (command){
            case CLEAR:
                cacheExecutor.clearCache(cacheName);
                break;
            case INVALIDATE:

        }
    }
}
