package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.manager.CacheManager;

import java.lang.reflect.Method;

/**
 * @author: 李俊平
 * @Date: 2022-06-16 23:26
 */
public abstract class AbstractCacheHandler implements CacheHandler{

    private final KeyGenerator keyGenerator;
    private final CacheManager cacheManager;

    public AbstractCacheHandler(KeyGenerator keyGenerator, CacheManager cacheManager) {
        this.keyGenerator = keyGenerator;
        this.cacheManager = cacheManager;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }

    protected String generateKey(Method method, Object[] args){
        return keyGenerator.generate(method, args);
    }
}
