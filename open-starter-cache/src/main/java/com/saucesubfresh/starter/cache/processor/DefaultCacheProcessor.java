package com.saucesubfresh.starter.cache.processor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lijunping on 2022/5/25
 */
public class DefaultCacheProcessor extends AbstractCacheProcessor {

    public DefaultCacheProcessor(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public Object handlerCacheable(Supplier<Object> callback, String cacheName, String cacheKey) throws Throwable {
        Object value;
        final ClusterCache cache = super.getCache(cacheName);
        value = cache.get(cacheKey);
        if (Objects.nonNull(value)){
            return super.fromStoreValue(value);
        }
        value = callback.get();
        cache.put(cacheKey, value);
        return value;
    }

    @Override
    public void handlerCacheEvict(String cacheName, String cacheKey) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.evict(cacheKey);
    }

    @Override
    public void handlerCacheClear(String cacheName) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.clear();
    }

    @Override
    public void handlerCachePut(String cacheName, String cacheKey, Object cacheValue) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.put(cacheKey, cacheValue);
    }

}
