package com.saucesubfresh.starter.cache.core;

/**
 * @author lijunping on 2022/5/24
 */
public interface ClusterCache{
    /**
     * 获取缓存
     * @param key
     * @return
     */
    Object get(Object key);

    /**
     * 添加缓存
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     * 根据 key 清除
     * @param key
     */
    void evict(Object key);

    /**
     * 清空该 cacheName 下的缓存
     */
    void clear();
}
