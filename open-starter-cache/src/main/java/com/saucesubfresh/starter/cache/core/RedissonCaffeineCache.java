package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheMessageCommand;
import com.saucesubfresh.starter.cache.message.CacheMessageListener;
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

    private final String cacheName;
    /**
     * RLocalCachedMap 自带本地和远程缓存
     */
    private final RLocalCachedMap<Object, Object> map;

    public RedissonCaffeineCache(String cacheName,
                                 String namespace,
                                 CacheConfig cacheConfig,
                                 RedissonClient redissonClient,
                                 CacheMessageListener messageListener) {
        super(new ConcurrentStatsCounter(), messageListener);
        this.cacheName = cacheName;
        String cacheHashKey = super.generate(namespace, cacheName);
        LocalCachedMapOptions<Object, Object> options = LocalCachedMapOptions.defaults();
        options.cacheProvider(LocalCachedMapOptions.CacheProvider.CAFFEINE);
        options.cacheSize(cacheConfig.getMaxSize());
        options.timeToLive(cacheConfig.getTtl());
        options.maxIdle(cacheConfig.getMaxIdleTime());
        this.map = redissonClient.getLocalCachedMap(cacheHashKey, options);
    }

    @Override
    public void preloadCache() {
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.UPDATE);
        //cacheMessage.setKey(key);
        //cacheMessage.setValue(value);
        super.publish(cacheMessage);
        map.preloadCache();
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
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.UPDATE);
        cacheMessage.setKey(key);
        cacheMessage.setValue(value);
        super.publish(cacheMessage);
        this.afterPut();
    }

    @Override
    public void evict(Object key) {
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.INVALIDATE);
        cacheMessage.setKey(key);
        super.publish(cacheMessage);
        map.fastRemove(key);
    }

    @Override
    public void clear() {
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.CLEAR);
        super.publish(cacheMessage);
        map.clear();
    }
}
