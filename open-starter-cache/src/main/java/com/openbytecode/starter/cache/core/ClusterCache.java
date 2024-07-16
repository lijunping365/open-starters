/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.cache.core;

import com.openbytecode.starter.cache.stats.CacheStats;

import java.util.Set;

/**
 * @author lijunping
 */
public interface ClusterCache{

    /**
     * 二级缓存数据加载到一级缓存
     * @param count - size of keys batch
     */
    void preloadCache(int count);

    /**
     * 获取统计数据
     * @return
     */
    CacheStats getStats();
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
    Object put(Object key, Object value);

    /**
     * 根据 key 清除
     * @param key
     */
    void evict(Object key);

    /**
     * 清空该 cacheName 下的缓存
     */
    void clear();

    /**
     * 获取缓存 key 数量
     */
    long getCacheKeyCount();

    /**
     * 获取缓存 key set
     * @param pattern - key pattern
     * @param count - size of keys batch
     */
    Set<Object> getCacheKeySet(String pattern, int count);
}
