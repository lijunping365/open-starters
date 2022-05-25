package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.annotation.CacheEvict;
import com.saucesubfresh.starter.cache.annotation.Cacheable;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.LocalCache;
import com.saucesubfresh.starter.cache.generator.KeyGenerator;

/**
 * @author lijunping on 2022/5/25
 */
public class DefaultCacheHandler<K,V> implements CacheHandler {

    private final KeyGenerator keyGenerator;
    private final LocalCache<K,V>  localCache;
    private final ClusterCache<K,V> clusterCache;

    public DefaultCacheHandler(KeyGenerator keyGenerator,
                               LocalCache<K, V> localCache,
                               ClusterCache<K, V> clusterCache) {
        this.keyGenerator = keyGenerator;
        this.localCache = localCache;
        this.clusterCache = clusterCache;
    }

    @Override
    public Object handlerCacheable(Cacheable cacheAble, Class<?> returnType, Object[] args, InvokeCallBack callBack) {
        return null;
    }

    @Override
    public void handlerCacheEvict(CacheEvict cacheEvict, Object[] args) throws Throwable {

    }

}
