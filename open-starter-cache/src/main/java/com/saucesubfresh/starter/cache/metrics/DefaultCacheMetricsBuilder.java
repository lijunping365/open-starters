package com.saucesubfresh.starter.cache.metrics;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import com.saucesubfresh.starter.cache.stats.CacheStats;

/**
 * @author lijunping on 2022/6/24
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
