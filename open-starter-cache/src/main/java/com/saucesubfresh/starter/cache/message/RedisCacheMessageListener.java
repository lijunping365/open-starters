package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
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

    private final CacheProperties cacheProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageListener(CacheExecutor cacheExecutor,
                                     CacheProperties cacheProperties,
                                     RedisTemplate<String, Object> redisTemplate) {
        super(cacheExecutor);
        this.cacheProperties = cacheProperties;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String namespace = cacheProperties.getNamespace();
        try {
            redisTemplate.convertAndSend(namespace, message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info("receive a redis topic message, sync local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        super.onMessage(cacheMessage);
    }
}
