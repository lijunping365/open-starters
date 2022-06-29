package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class RedissonCacheMessageProducer implements CacheMessageProducer{

    private final RTopic topic;

    public RedissonCacheMessageProducer(RedissonClient redissonClient, CacheProperties properties) {
        String namespace = properties.getNamespace();
        this.topic = redissonClient.getTopic(namespace);
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        try {
            topic.publish(message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
