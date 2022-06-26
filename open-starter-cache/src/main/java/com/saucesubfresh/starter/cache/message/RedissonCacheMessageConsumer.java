package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.listener.MessageListener;


/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:26
 */
@Slf4j
public class RedissonCacheMessageConsumer extends AbstractCacheMessageConsumer implements MessageListener<CacheMessage> {

    public RedissonCacheMessageConsumer(CacheExecutor cacheExecutor) {
        super(cacheExecutor);
    }

    @Override
    public void onMessage(CharSequence channel, CacheMessage message) {
        log.debug("receive a redis topic message, sync local cache, the cacheName is {}, the key is {}",
                message.getCacheName(), message.getKey());
        super.onMessage(message);
    }
}
