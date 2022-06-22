package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.properties.CacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.cache.Cache;

import java.util.Optional;

/**
 * @author lijunping on 2022/6/9
 */
@Slf4j
public class ClusterCacheProvider extends AbstractClusterCache {

    private final Cache cache;

    public ClusterCacheProvider(String cacheName, RedissonClient redissonClient, CacheConfig cacheConfig) {
        LocalCachedMapOptions<Object, Object> options = LocalCachedMapOptions.defaults();
        options.cacheProvider(LocalCachedMapOptions.CacheProvider.CAFFEINE);
        RMap<Object, Object> map = redissonClient.getLocalCachedMap(cacheName, options);
        this.cache = new RedissonCache(map, cacheConfig.isAllowNullValues());
    }

    @Override
    public Object get(Object key) {
        return Optional.ofNullable(cache.get(key)).map(Cache.ValueWrapper::get).orElse(null);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.evict(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

}
