package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;

import java.util.function.Supplier;

/**
 * @author lijunping on 2022/5/25
 */
public interface CacheHandler {

    Object handlerCacheable(Cacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) throws Throwable;

    void handlerCacheEvict(CacheEvict cacheEvict, Object[] args) throws Throwable;
}
