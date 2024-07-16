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
package com.openbytecode.starter.cache.metrics;

import com.openbytecode.starter.cache.core.ClusterCache;
import com.openbytecode.starter.cache.properties.CacheProperties;
import com.openbytecode.starter.cache.manager.CacheManager;
import com.openbytecode.starter.cache.stats.CacheStats;

/**
 * @author lijunping
 */
public class DefaultCacheMetricsBuilder implements CacheMetricsBuilder {

    private final CacheProperties properties;

    private final CacheManager cacheManager;

    public DefaultCacheMetricsBuilder(CacheProperties properties, CacheManager manager) {
        this.properties = properties;
        this.cacheManager = manager;
    }

    @Override
    public CacheMetrics buildCacheMetrics(String cacheName) {
        final ClusterCache cache = cacheManager.getCache(cacheName);
        final CacheStats stats = cache.getStats();
        return CacheMetrics.builder()
                .namespace(properties.getNamespace())
                .cacheName(cacheName)
                .hitCount(stats.getHitCount())
                .missCount(stats.getMissCount())
                .requestCount(stats.requestCount())
                .putCount(stats.getPutsCount())
                .hitRate(stats.hitRate())
                .missRate(stats.missRate())
                .build();
    }

}
