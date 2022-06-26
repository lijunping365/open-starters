package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheMessageProducer;
import com.saucesubfresh.starter.cache.stats.ConcurrentStatsCounter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;

/**
 * @author lijunping on 2022/6/9
 */
@Slf4j
public class RedissonCaffeineCache extends AbstractClusterCache{

    /**
     * RLocalCachedMap 自带本地和远程缓存
     */
    private final RLocalCachedMap<Object, Object> map;

    public RedissonCaffeineCache(String cacheName,
                                 CacheConfig cacheConfig,
                                 RedissonClient redissonClient,
                                 CacheMessageProducer messageProducer) {
        super(new ConcurrentStatsCounter(), messageProducer);
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
        value = toValueWrapper(value);
        this.afterRead(value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        value = toStoreValue(value);
        map.fastPut(key, value);
        super.publish(new CacheMessage());
        this.afterPut();
    }

    @Override
    public void evict(Object key) {
        super.publish(new CacheMessage());
        map.fastRemove(key);
    }

    @Override
    public void clear() {
        super.publish(new CacheMessage());
        map.clear();
    }
}
