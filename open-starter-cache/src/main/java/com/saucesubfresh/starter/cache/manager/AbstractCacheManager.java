package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.properties.CacheProperties;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:57
 */
public abstract class AbstractCacheManager implements CacheManager{

    protected final ConcurrentMap<String, ClusterCache> cacheMap = new ConcurrentHashMap<>(16);

    private static final String SAM = ":";

    private final CacheProperties properties;

    protected AbstractCacheManager(CacheProperties properties) {
        this.properties = properties;
    }

    @Override
    public ClusterCache getCache(String cacheName) {
        String namespace = properties.getNamespace();
        cacheName = generate(namespace, cacheName);
        ClusterCache cache = cacheMap.get(cacheName);
        if (Objects.nonNull(cache)){
            return cache;
        }

        cache = this.createCache(cacheName);
        ClusterCache oldCache = cacheMap.putIfAbsent(cacheName, cache);
        return oldCache == null ? cache : oldCache;
    }

    protected String generate(String namespace, String cacheName){
        return namespace + SAM + cacheName;
    }

    protected abstract ClusterCache createCache(String cacheName);
}
