package com.saucesubfresh.starter.cache.core;

/**
 * @author lijunping on 2022/5/24
 */
public interface Cache<K, V> {

    /**
     * 获取缓存
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 添加缓存
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * 清除
     * @param key
     */
    void evict(K key);
}
