package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;

/**
 * 缓存注解处理器
 * @see Cacheable
 * @see CacheEvict
 *
 * @author lijunping on 2022/5/25
 */
public interface CacheAnnotationHandler {

    Object handlerCacheable(Cacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) throws Throwable;

    void handlerCacheEvict(CacheEvict cacheEvict, Object[] args) throws Throwable;
}
