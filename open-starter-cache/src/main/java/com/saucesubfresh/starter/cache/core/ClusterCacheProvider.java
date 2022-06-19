package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.saucesubfresh.starter.cache.domain.ValueWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lijunping on 2022/6/9
 */
@Slf4j
public class ClusterCacheProvider extends AbstractClusterCache {

    private final String cacheName;
    private final RedissonClient redissonClient;
    private final Cache<Object, Object> caffeineCache;

    public ClusterCacheProvider(String cacheName, RedissonClient redissonClient) {
        this.cacheName = cacheName;
        this.redissonClient = redissonClient;
    }

    @Override
    public Object get(Object key) {
        Object value = map.get(key);

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
}
