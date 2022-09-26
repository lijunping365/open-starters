/*
 * Copyright © 2022 organization SauceSubFresh
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
package com.saucesubfresh.starter.cache.metrics;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import com.saucesubfresh.starter.cache.stats.CacheStats;

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
