package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.LocalCache;
import com.saucesubfresh.starter.cache.core.RemoteCache;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 13:38
 */
public class DefaultCacheManager<K,V> extends AbstractCacheManager<K,V> {

    public DefaultCacheManager(LocalCache<K, V> localCache, RemoteCache<K, V> remoteCache) {
        super(localCache, remoteCache);
    }
}
