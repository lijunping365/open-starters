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

import com.openbytecode.starter.cache.domain.NullValue;
import com.openbytecode.starter.cache.factory.CacheConfig;
import com.openbytecode.starter.cache.stats.CacheStats;
import com.openbytecode.starter.cache.stats.StatsCounter;

/**
 * @author lijunping
 */
public abstract class AbstractClusterCache implements ClusterCache {

    private static final String SAM = ":";
    private final CacheConfig cacheConfig;
    private final StatsCounter statsCounter;

    public AbstractClusterCache(CacheConfig cacheConfig, StatsCounter statsCounter) {
        this.cacheConfig = cacheConfig;
        this.statsCounter = statsCounter;
    }

    @Override
    public CacheStats getStats() {
        return statsCounter.snapshot();
    }

    /**
     * <p>
     *     toValueWrapper
     * </p>
     */
    protected Object toValueWrapper(Object value) {
        if (value != null && value.getClass().getName().equals(NullValue.class.getName())) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    /**
     * <p>
     *     toStoreValue
     * </p>
     */
    protected Object toStoreValue(Object userValue) {
        if (cacheConfig.isAllowNullValues() && userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

    /**
     * <p>
     *     在 Get 之后增加命中率
     * </p>
     */
    protected void afterRead(Object value){
        if (value == null) {
            statsCounter.recordMisses(1);
        } else {
            statsCounter.recordHits(1);
        }
    }

    /**
     * <p>
     *     再 Put 之后增加存放次数
     * </p>
     */
    protected void afterPut(){
        statsCounter.recordPuts(1);
    }

    /**
     * <p>
     *     这里解释下缓存名称为什么要拼接命名空间
     *
     *     为了避免多个应用使用同一个 redis 时 cacheName 相同
     * </p>
     *
     * @param namespace 命名空间
     * @param cacheName 缓存名称
     * @return 命名空间 + 缓存名称
     */
    protected String generate(String namespace, String cacheName){
        return namespace.concat(SAM).concat(cacheName);
    }
}
