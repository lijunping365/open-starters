package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;

/**
 * 管理 cache
 * @author: 李俊平
 * @Date: 2022-05-29 13:36
 */
public interface CacheManager {

    /**
     * 获取 ClusterCache
     * @param namespace 应用隔离
     * @param cacheName 缓存隔离
     * @return ClusterCache
     */
    ClusterCache getCache(String namespace, String cacheName);
}
