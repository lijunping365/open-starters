package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:57
 */
@Slf4j
public abstract class AbstractCacheManager implements CacheManager{

    private final ConfigFactory configFactory;
    private final ConcurrentMap<String, ClusterCache> cacheMap = new ConcurrentHashMap<>(16);

    protected AbstractCacheManager(ConfigFactory configFactory) {
        this.configFactory = configFactory;
    }

    @Override
    public ClusterCache getCache(String cacheName) {
        ClusterCache cache = cacheMap.get(cacheName);
        if (Objects.nonNull(cache)){
            return cache;
        }

        CacheConfig cacheConfig = configFactory.create(cacheName);
        cache = this.createCache(cacheName, cacheConfig);
        ClusterCache oldCache = cacheMap.putIfAbsent(cacheName, cache);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    protected abstract ClusterCache createCache(String cacheName, CacheConfig cacheConfig);
}
