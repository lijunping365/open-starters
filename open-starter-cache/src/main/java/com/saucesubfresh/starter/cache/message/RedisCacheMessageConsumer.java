package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:26
 */
@Slf4j
public class RedisCacheMessageConsumer extends AbstractCacheMessageConsumer implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageConsumer(CacheExecutor cacheExecutor, RedisTemplate<String, Object> redisTemplate) {
        super(cacheExecutor);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.debug("receive a redis topic message, sync local cache, the cacheName is {}, the key is {}",
                cacheMessage.getCacheName(), cacheMessage.getKey());
        super.handlerMessage(cacheMessage);
    }
}
