package com.saucesubfresh.starter.cache.metrics;

/**
 * @author lijunping on 2022/6/24
 */
public interface CacheMetricsBuilder {

    CacheMetrics buildCacheMetrics(String cacheName);
}
