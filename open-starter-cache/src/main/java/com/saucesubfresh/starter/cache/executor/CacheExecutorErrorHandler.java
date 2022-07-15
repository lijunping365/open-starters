package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.exception.CacheException;

/**
 * 执行失败策略
 * @author lijunping on 2022/6/27
 */
public interface CacheExecutorErrorHandler {

    /**
     * 缓存操作执行失败后被调用
     *
     * @param cacheException 异常信息
     */
    void onExecuteError(CacheException cacheException);
}
