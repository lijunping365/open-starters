package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheMessageCommand;
import com.saucesubfresh.starter.cache.message.CacheMessageListener;
import com.saucesubfresh.starter.cache.stats.ConcurrentStatsCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 注意点：
 * 1. 除了查询操作外，我们都是先执行二级缓存，然后再执行一级缓存
 *
 * 比如清除操作，先清除 redis 中缓存数据，然后再清除 caffeine 缓存，避免短时间内如果先清除 caffeine 缓存后其他请求会再从 redis 里加载到 caffeine 中
 *
 * @author: 李俊平
 * @Date: 2022-06-18 23:33
 */
@Slf4j
public class RedisCaffeineCache extends AbstractClusterCache {

    private final String cacheName;
    private final String cacheHashKey;
    private final Cache<Object, Object> cache;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCaffeineCache(String cacheName,
                              String namespace,
                              CacheConfig cacheConfig,
                              CacheMessageListener messageProducer,
                              RedisTemplate<String, Object> redisTemplate) {
        super(new ConcurrentStatsCounter(), messageProducer);
        this.cacheName = cacheName;
        this.cacheHashKey = super.generate(namespace, cacheName);
        this.redisTemplate = redisTemplate;
        this.cache = Caffeine.newBuilder()
                .maximumSize(cacheConfig.getMaxSize())
                .expireAfterWrite(cacheConfig.getTtl(), TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void preloadCache() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheHashKey);
        if (CollectionUtils.isEmpty(entries)){
            return;
        }
        entries.forEach((key, value)->{
            CacheMessage cacheMessage = new CacheMessage();
            cacheMessage.setCacheName(cacheName);
            cacheMessage.setCommand(CacheMessageCommand.UPDATE);
            cacheMessage.setKey(key);
            cacheMessage.setValue(value);
            super.publish(cacheMessage);
            cache.put(key, value);
        });
    }

    @Override
    public Object get(Object key) {
        Object value = cache.getIfPresent(key);
        if (value != null) {
            log.debug("get cache from caffeine, the key is : {}", key);
        } else {
            value = redisTemplate.opsForHash().get(cacheHashKey, key);
            if (value != null){
                cache.put(key, value);
            }
        }
        value = toValueWrapper(value);
        this.afterRead(value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        value = toStoreValue(value);
        redisTemplate.opsForHash().put(cacheHashKey, key, value);
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.UPDATE);
        cacheMessage.setKey(key);
        cacheMessage.setValue(value);
        super.publish(cacheMessage);
        cache.put(key, value);
        this.afterPut();
    }

    @Override
    public void evict(Object key) {
        redisTemplate.opsForHash().delete(cacheHashKey, key);
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.INVALIDATE);
        cacheMessage.setKey(key);
        super.publish(cacheMessage);
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(cacheHashKey);
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setCacheName(cacheName);
        cacheMessage.setCommand(CacheMessageCommand.CLEAR);
        super.publish(cacheMessage);
        cache.invalidateAll();
    }
}
