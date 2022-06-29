package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class RedisCacheMessageProducer implements CacheMessageProducer {

    private final CacheProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheMessageProducer(CacheProperties properties,
                                     RedisTemplate<String, Object> redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String namespace = properties.getNamespace();
        try {
            redisTemplate.convertAndSend(namespace, message);
            log.info("发送缓存同步消息成功");
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
