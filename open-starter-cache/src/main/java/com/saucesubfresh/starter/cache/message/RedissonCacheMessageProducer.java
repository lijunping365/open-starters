package com.saucesubfresh.starter.cache.message;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class RedissonCacheMessageProducer implements CacheMessageProducer{

    private final RedissonClient redissonClient;

    public RedissonCacheMessageProducer(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String topicName = message.getTopic();
        try {
            RTopic topic = redissonClient.getTopic(topicName);
            long receivedMsgClientNum = topic.publish(message);
            log.info("接收到消息的客户端数量是 {}", receivedMsgClientNum);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
