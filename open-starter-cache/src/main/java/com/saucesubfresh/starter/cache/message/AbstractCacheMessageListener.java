package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.properties.CacheProperties;

import java.util.Objects;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:38
 */
public abstract class AbstractCacheMessageListener implements CacheMessageListener{

    private final CacheExecutor cacheExecutor;
    private final CacheProperties properties;

    protected AbstractCacheMessageListener(CacheExecutor cacheExecutor, CacheProperties properties) {
        this.cacheExecutor = cacheExecutor;
        this.properties = properties;
    }

    @Override
    public void onMessage(CacheMessage message) {
        Long instanceId = properties.getInstanceId();
        if (Objects.equals(instanceId, message.getInstanceId())){
            return;
        }
        cacheExecutor.execute(message);
    }
}
