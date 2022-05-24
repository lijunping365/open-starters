package com.saucesubfresh.starter.cache.core;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 集群缓存默认实现（redis cache）
 * @author lijunping on 2022/5/24
 */
public class RedisCache<K, V> implements ClusterCache<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    public RedisCache(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
