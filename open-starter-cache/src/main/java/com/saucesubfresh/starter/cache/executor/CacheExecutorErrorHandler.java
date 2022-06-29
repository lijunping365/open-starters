package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.exception.CacheException;

/**
 * 执行失败策略
 * @author lijunping on 2022/6/27
 */
public interface CacheExecutorErrorHandler {

    void onExecuteError(CacheException cacheException);
}
