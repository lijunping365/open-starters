package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.manager.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:26
 */
public class KafkaCacheMessageConsumer implements CacheMessageConsumer, MessageListener {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;

    public KafkaCacheMessageConsumer(CacheManager cacheManager, RedisTemplate<String, Object> redisTemplate) {
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void handlerMessage(CacheMessage message) {
        String cacheName = message.getCacheName();
        CacheMessageCommand command = message.getCommand();

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
