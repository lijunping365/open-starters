package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.OpenCacheEvict;
import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author lijunping on 2022/5/25
 */
public class DefaultCacheAnnotationHandler<K,V> implements CacheAnnotationHandler {

    private final KeyGenerator keyGenerator;
    private final CacheManager<K,V> cacheManager;

    public DefaultCacheAnnotationHandler(KeyGenerator keyGenerator, CacheManager<K, V> cacheManager) {
        this.keyGenerator = keyGenerator;
        this.cacheManager = cacheManager;
    }


    @Override
    public Object handlerCacheable(OpenCacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) {
        return null;
    }

    @Override
    public void handlerCacheEvict(OpenCacheEvict openCacheEvict, Object[] args) throws Throwable {

    }

}
