package com.saucesubfresh.starter.cache.message;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public class RedissonCacheMessageProducer implements CacheMessageProducer{

    private final RedissonClient redissonClient;

    public RedissonCacheMessageProducer(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String topicName = message.getTopic();
        RTopic topic = redissonClient.getTopic(topicName);
        topic.publish(message);
    }
}
