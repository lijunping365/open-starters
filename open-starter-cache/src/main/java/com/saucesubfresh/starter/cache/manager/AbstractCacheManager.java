package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.LocalCache;
import com.saucesubfresh.starter.cache.core.RemoteCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:57
 */
public abstract class AbstractCacheManager<K,V> implements CacheManager<K,V>, InitializingBean {

    private final LocalCache<K,V> localCache;
    private final RemoteCache<K,V> remoteCache;

    public AbstractCacheManager(LocalCache<K, V> localCache, RemoteCache<K, V> remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void evict(K key) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationUtils.getAnnotationAttributes()
    }
}
