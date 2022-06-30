package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.exception.CacheException;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping on 2022/6/27
 */
@Slf4j
public class DefaultCacheExecutorErrorHandler implements CacheExecutorErrorHandler {

    @Override
    public void onExecuteError(CacheException cacheException) {
        CacheMessage message = cacheException.getCacheMessage();
        final String cacheName = message.getCacheName();
        CacheCommand command = message.getCommand();
        log.error("缓存操作执行异常 = {}，cacheName = {}, 异常原因 = {}", command.name(), cacheName, message);
    }
}
