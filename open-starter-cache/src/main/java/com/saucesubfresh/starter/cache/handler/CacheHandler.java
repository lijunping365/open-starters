package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.OpenCacheClear;
import com.saucesubfresh.starter.cache.annotation.OpenCacheEvict;
import com.saucesubfresh.starter.cache.annotation.OpenCacheable;

/**
 * 缓存注解处理器
 * @see OpenCacheable
 * @see OpenCacheEvict
 *
 * @author lijunping on 2022/5/25
 */
public interface CacheHandler {

    Object handlerCacheable(OpenCacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) throws Throwable;

    void handlerCacheEvict(OpenCacheEvict openCacheEvict, Object[] args) throws Throwable;

    void handlerCacheClear(OpenCacheClear openCacheClear, Object[] args) throws Throwable;
}
