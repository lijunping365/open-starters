package com.saucesubfresh.starter.cache.processor;

import java.util.function.Supplier;

/**
 * 缓存注解处理器
 *
 * @author lijunping on 2022/5/25
 */
public interface CacheProcessor {

    Object handlerCacheable(Supplier<Object> callback, String cacheName, String cacheKey) throws Throwable;

    void handlerCacheEvict(String cacheName, String cacheKey) throws Throwable;

    void handlerCacheClear(String cacheName) throws Throwable;

    void handlerCachePut(String cacheName, String cacheKey, Object cacheValue) throws Throwable;
}
