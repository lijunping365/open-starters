package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.LocalCache;

import java.util.function.Supplier;

/**
 * @author lijunping on 2022/5/25
 */
public class DefaultCacheHandler implements CacheHandler{

    private final LocalCache localCache;
    private final ClusterCache clusterCache;

    public <K,V> DefaultCacheHandler(LocalCache<K,V> localCache, ClusterCache<K,V> clusterCache) {
        this.localCache = localCache;
        this.clusterCache = clusterCache;
    }


    @Override
    public Object handlerCacheable(Cacheable cacheAble, Class<?> returnType, Object[] args, Supplier<Object> supplier) {
        return null;
    }

    @Override
    public void handlerCacheEvict(CacheEvict cacheEvict) {

    }
}
