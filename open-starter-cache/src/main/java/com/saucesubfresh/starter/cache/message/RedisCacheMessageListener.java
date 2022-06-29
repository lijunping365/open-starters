package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class RedisCacheMessageListener extends AbstractCacheMessageListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageListener(CacheExecutor cacheExecutor,
                                     RedisTemplate<String, Object> redisTemplate) {
        super(cacheExecutor);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info("received a message, cacheName={}", cacheMessage.getCacheName());
        super.onMessage(cacheMessage);
    }
}
