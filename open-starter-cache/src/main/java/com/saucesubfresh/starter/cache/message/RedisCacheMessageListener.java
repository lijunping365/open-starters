package com.saucesubfresh.starter.cache.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class RedisCacheMessageListener implements CacheMessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageListener(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String topic = message.getTopic();
        try {
            redisTemplate.convertAndSend(topic, message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(CacheMessage message) {
        redisTemplate
    }
}
