package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:57
 */
public abstract class AbstractCacheManager implements CacheManager{

    protected final ConcurrentMap<String, ClusterCache> cacheMap = new ConcurrentHashMap<>(16);

}
