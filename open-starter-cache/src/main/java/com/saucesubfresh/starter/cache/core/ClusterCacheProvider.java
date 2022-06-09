package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.ValueWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author lijunping on 2022/6/9
 */
@Slf4j
public class ClusterCacheProvider extends AbstractClusterCache {

    private final String cacheName;
    private final LocalCache localCache;
    private final RemoteCache remoteCache;

    public ClusterCacheProvider(String cacheName, LocalCache localCache, RemoteCache remoteCache) {
        super(true);
        this.cacheName = cacheName;
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }

    @Override
    public Object get(Object key) {
        Object obj = localCache.get(key);
        if (Objects.nonNull(obj)){
            log.info("Get data from LocalCache");
            return obj;
        }
        obj = remoteCache.get(key);
        if (Objects.nonNull(obj)){
            log.info("Get data from RemoteCache");
            localCache.put(key, obj);
        }
        return obj;

    }

    @Override
    protected ValueWrapper lookup(Object key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        localCache.put(key, toStoreValue(value));
        // null对象只存在 LocalCache 中一份就够了，不用存 RemoteCache 了
        if (Objects.isNull(value)){
            return;
        }
        Optional<Long> expireOpt = Optional.ofNullable(doubleCacheConfig)
                .map(DoubleCacheConfig::getRedisExpire);
        if (expireOpt.isPresent()){
            redisTemplate.opsForValue().set(redisKey,toStoreValue(value),
                    expireOpt.get(), TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(redisKey,toStoreValue(value));
        }
    }

    @Override
    public void evict(Object key) {
        remoteCache.evict(key);
        localCache.evict(key);
    }

    @Override
    public void clear() {
        remoteCache.clear();
        localCache.clear();
    }
}
