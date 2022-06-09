package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.*;
import com.saucesubfresh.starter.cache.properties.CacheConfig;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 13:38
 */
public class DefaultCacheManager extends AbstractCacheManager implements InitializingBean {

    private static final String JOINER = ":";

    private final RedissonClient redissonClient;

    public DefaultCacheManager(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public ClusterCache getCache(String namespace, String cacheName) {
        String cacheMapKey = namespace + JOINER + cacheName;
        ClusterCache cache = cacheMap.get(cacheMapKey);
        if (Objects.nonNull(cache)){
            return cache;
        }
        CacheConfig cacheConfig = new CacheConfig();

        cache = new ClusterCacheProvider(cacheMapKey, localCache, remoteCache);
        ClusterCache oldCache = cacheMap.putIfAbsent(cacheMapKey, cache);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
