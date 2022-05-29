package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author lijunping on 2022/5/25
 */
public class DefaultCacheAnnotationHandler<K,V> implements CacheAnnotationHandler {

    private final KeyGenerator keyGenerator;
    private final CacheManager cacheManager;

    public DefaultCacheAnnotationHandler(KeyGenerator keyGenerator, CacheManager cacheManager) {
        this.keyGenerator = keyGenerator;
        this.cacheManager = cacheManager;
    }


    @Override
    public Object handlerCacheable(Cacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) {
        return null;
    }

    @Override
    public void handlerCacheEvict(CacheEvict cacheEvict, Object[] args) throws Throwable {

    }

}
