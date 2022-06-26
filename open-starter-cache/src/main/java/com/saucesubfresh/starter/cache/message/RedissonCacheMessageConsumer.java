package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import org.redisson.api.listener.MessageListener;


/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:26
 */
public class RedissonCacheMessageConsumer extends AbstractCacheMessageConsumer implements MessageListener {


    protected RedissonCacheMessageConsumer(CacheExecutor cacheExecutor) {
        super(cacheExecutor);
    }

    @Override
    public void onMessage(CharSequence channel, Object msg) {

    }
}
