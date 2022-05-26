package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 集群缓存默认实现（redis cache）
 * @author lijunping on 2022/5/24
 */
public class RedisCache<K, V> implements ClusterCache<K, V>, InitializingBean {

    private final CacheProperties cacheProperties;

    public RedisCache(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void evict(K key) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        //set key serializer
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        this.redisTemplate = (RedisTemplate<K, V>) redisTemplate;
    }
}
