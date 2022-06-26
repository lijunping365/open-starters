package com.saucesubfresh.starter.cache.message;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public class RedisCacheMessageProducer implements CacheMessageProducer{

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageProducer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String topic = message.getTopic();
        redisTemplate.convertAndSend(topic, message);
    }
}
