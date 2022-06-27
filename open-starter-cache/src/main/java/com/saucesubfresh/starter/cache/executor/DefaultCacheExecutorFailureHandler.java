package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.exception.CacheException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping on 2022/6/27
 */
@Slf4j
public class DefaultCacheExecutorFailureHandler implements CacheExecutorFailureHandler {

    @Override
    public void onExecuteFailureHandler(CacheException cacheException) {
        final String message = cacheException.getMessage();
        final String cacheName = cacheException.getCacheName();
        log.error("缓存操作执行异常 {}，异常原因 {}", cacheName, message);
    }
}
