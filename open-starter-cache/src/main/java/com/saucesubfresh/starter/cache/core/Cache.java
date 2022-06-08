package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.ValueWrapper;

/**
 * @author lijunping on 2022/5/24
 */
public interface Cache {

    /**
     * 获取缓存
     * @param key
     * @return
     */
    ValueWrapper get(Object key);

    /**
     * 添加缓存
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     * 清除
     * @param key
     */
    void evict(Object key);
}
