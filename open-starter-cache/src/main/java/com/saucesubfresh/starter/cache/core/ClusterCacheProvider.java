package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.factory.CacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lijunping on 2022/6/9
 */
@Slf4j
public class ClusterCacheProvider extends AbstractClusterCache {

    /**
     * RLocalCachedMap 自带本地和远程缓存
     */
    private final RLocalCachedMap<Object, Object> map;

    private final AtomicLong hits = new AtomicLong();

    private final AtomicLong puts = new AtomicLong();

    private final AtomicLong misses = new AtomicLong();

    public ClusterCacheProvider(String cacheName,
                                RedissonClient redissonClient,
                                CacheConfig cacheConfig) {
        LocalCachedMapOptions<Object, Object> options = LocalCachedMapOptions.defaults();
        options.cacheProvider(LocalCachedMapOptions.CacheProvider.CAFFEINE);
        options.cacheSize(cacheConfig.getMaxSize());
        options.timeToLive(cacheConfig.getTtl());
        options.maxIdle(cacheConfig.getMaxIdleTime());
        this.map = redissonClient.getLocalCachedMap(cacheName, options);
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
    }

    @Override
    public void put(Object key, Object value) {
        value = toStoreValue(value);
        map.fastPut(key, value);
        addCachePut();
    }

    @Override
    public void evict(Object key) {
        map.fastRemove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    /** The number of get requests that were satisfied by the cache.
     * @return the number of hits
     */
    long getCacheHits(){
        return hits.get();
    }

    /** A miss is a get request that is not satisfied.
     * @return the number of misses
     */
    long getCacheMisses(){
        return misses.get();
    }

    long getCachePuts() {
        return puts.get();
    }

    private void addCachePut() {
        puts.incrementAndGet();
    }

    private void addCacheHit(){
        hits.incrementAndGet();
    }

    private void addCacheMiss(){
        misses.incrementAndGet();
    }

}
