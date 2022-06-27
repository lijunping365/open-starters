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
public class RedissonCacheMessageListener implements CacheMessageListener {

    private final RTopic topic;

    public RedissonCacheMessageListener(RedissonClient redissonClient, CacheProperties cacheProperties) {
        String namespace = cacheProperties.getNamespace();
        this.topic = redissonClient.getTopic(namespace);
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        try {
            long receivedMsgClientNum = topic.publish(message);
            log.info("接收到消息的客户端数量是 {}", receivedMsgClientNum);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(CacheMessage message) {
        topic.addListener(CacheMessage.class, (channel, msg) -> {
            log.info("received a message, cacheName={}", msg.getCacheName());
            super.onMessage(msg);
        });
    }
}
