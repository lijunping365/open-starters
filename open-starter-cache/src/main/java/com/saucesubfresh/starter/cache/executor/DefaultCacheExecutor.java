package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping on 2022/6/16
 */
@Slf4j
public class DefaultCacheExecutor extends AbstractCacheExecutor {

    public DefaultCacheExecutor(CacheManager cacheManager, CacheExecutorErrorHandler errorHandler) {
        super(cacheManager, errorHandler);
    }

    @Override
    public void execute(CacheMessage message){
        CacheCommand command = message.getCommand();
        String cacheName = message.getCacheName();
        Object key = message.getKey();
        Object value = message.getValue();
        ClusterCache cache = super.getCache(cacheName);
        try {
            switch (command){
                case CLEAR:
                    cache.clear();
                    break;
                case INVALIDATE:
                    cache.evict(key);
                    break;
                case PRELOAD:
                    cache.preloadCache();
                    break;
                case UPDATE:
                    cache.put(key, value);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported Operation");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            onError(e.getMessage(), message);
        }
    }
}
