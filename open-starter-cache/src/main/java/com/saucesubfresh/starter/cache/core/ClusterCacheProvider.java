package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.starter.cache.domain.ValueWrapper;
import com.saucesubfresh.starter.cache.properties.CacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lijunping on 2022/6/9
 */
@Slf4j
public class ClusterCacheProvider extends AbstractClusterCache {

    private final String cacheName;
    private final RedissonClient redissonClient;
    private final Cache<Object, Object> caffeineCache;

    public ClusterCacheProvider(String cacheName, RedissonClient redissonClient, CacheConfig cacheConfig) {
        this.cacheName = cacheName;
        this.redissonClient = redissonClient;
        this.caffeineCache = initCaffeineCache(cacheConfig);
    }

    @Override
    public Object get(Object key) {
        Object value = caffeineCache.getIfPresent(key);
        if (value == null) {
            addCacheMiss();
        } else {
            addCacheHit();
        }
        return toValueWrapper(value);
//        ValueWrapper obj = localCache.get(key);
//        if (Objects.nonNull(obj)){
//            log.info("Get data from LocalCache");
//            return obj;
//        }
//        obj = remoteCache.get(key);
//        if (Objects.nonNull(obj)){
//            log.info("Get data from RemoteCache");
//            localCache.put(key, obj);
//        }
        return null;

    }

    @Override
    public void put(Object key, Object value) {
        //localCache.put(key, wrapper(value));
    }

    @Override
    public void evict(Object key) {
        //remoteCache.evict(key);
        //localCache.evict(key);
    }

    @Override
    public void clear() {
        //remoteCache.clear();
        //localCache.clear();
    }

    private Cache<Object, Object> initCaffeineCache(CacheConfig cacheConfig){
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
        if (options.getTimeToLiveInMillis() > 0) {
            caffeineBuilder.expireAfterWrite(options.getTimeToLiveInMillis(), TimeUnit.MILLISECONDS);
        }
        if (options.getMaxIdleInMillis() > 0) {
            caffeineBuilder.expireAfterAccess(options.getMaxIdleInMillis(), TimeUnit.MILLISECONDS);
        }
        if (options.getCacheSize() > 0) {
            caffeineBuilder.maximumSize(options.getCacheSize());
        }
        if (options.getEvictionPolicy() == LocalCachedMapOptions.EvictionPolicy.SOFT) {
            caffeineBuilder.softValues();
        }
        if (options.getEvictionPolicy() == LocalCachedMapOptions.EvictionPolicy.WEAK) {
            caffeineBuilder.weakValues();
        }
        return caffeineBuilder.build();
    }

    public int getSize(){
        return caffeineCache.asMap().size();
    }
}
