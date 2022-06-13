package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.OpenCacheClear;
import com.saucesubfresh.starter.cache.annotation.OpenCacheEvict;
import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.properties.CacheProperties;

import java.util.Objects;

/**
 * @author lijunping on 2022/5/25
 */
public class DefaultCacheAnnotationHandler implements CacheAnnotationHandler {

    private final CacheProperties properties;
    private final KeyGenerator keyGenerator;
    private final CacheManager cacheManager;

    public DefaultCacheAnnotationHandler(CacheProperties properties, KeyGenerator keyGenerator, CacheManager cacheManager) {
        this.properties = properties;
        this.keyGenerator = keyGenerator;
        this.cacheManager = cacheManager;
    }

    @Override
    public Object handlerCacheable(OpenCacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) throws Throwable{
        Object value;
        final String cacheName = cacheAble.cacheName();
        final String cacheKey = cacheAble.cacheKey();
        final String namespace = properties.getNamespace();
        final ClusterCache cache = cacheManager.getCache(cacheName);
        value = cache.get(cacheKey);
        if (Objects.isNull(value)){
            value = callBack.invoke();
        }
        if (Objects.nonNull(value)){
            cache.put(cacheKey, value);
        }
        return value;
    }

    @Override
    public void handlerCacheEvict(OpenCacheEvict cacheEvict, Object[] args) throws Throwable {
        final String namespace = properties.getNamespace();
        final String cacheName = cacheEvict.cacheName();
        final String cacheKey = cacheEvict.cacheKey();
        final ClusterCache cache = cacheManager.getCache(cacheName);
        cache.evict(cacheKey);
    }

    @Override
    public void handlerCacheClear(OpenCacheClear cacheClear, Object[] args) throws Throwable {
        final String namespace = properties.getNamespace();
        final String cacheName = cacheClear.cacheName();
        final ClusterCache cache = cacheManager.getCache(cacheName);
        cache.clear();
    }

}
