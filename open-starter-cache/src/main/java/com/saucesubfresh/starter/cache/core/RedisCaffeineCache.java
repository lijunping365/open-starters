package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.stats.ConcurrentStatsCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

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
    private final Cache<Object, Object> cache;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCaffeineCache(String cacheName,
                              CacheConfig cacheConfig,
                              RedisTemplate<String, Object> redisTemplate) {
        super(new ConcurrentStatsCounter());
        this.cacheName = cacheName;
        this.redisTemplate = redisTemplate;
        this.cache = Caffeine.newBuilder()
                .maximumSize(cacheConfig.getMaxSize())
                .expireAfterWrite(cacheConfig.getTtl(), TimeUnit.SECONDS)
                .build();
    }

    @Override
    public Object get(Object key) {
        Object value = cache.getIfPresent(key);
        if (value != null) {
            log.debug("get cache from caffeine, the key is : {}", key);
        } else {
            value = redisTemplate.opsForHash().get(cacheName, key);
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
        redisTemplate.opsForHash().put(cacheName, key, value);
        // 发送同步消息
        cache.put(key, value);
        this.afterPut();
    }

    @Override
    public void evict(Object key) {
        redisTemplate.opsForHash().delete(cacheName, key);
        // 发送同步消息
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(cacheName);
        // 发送同步消息
        cache.invalidateAll();
    }
}
