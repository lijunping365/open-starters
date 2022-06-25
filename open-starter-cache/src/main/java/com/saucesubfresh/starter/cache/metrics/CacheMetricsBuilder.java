package com.saucesubfresh.starter.cache.metrics;

/**
 * 缓存指标信息构建者
 *
 * @author lijunping on 2022/6/24
 */
public interface CacheMetricsBuilder {

    /**
     * 构建缓存执行信息
     *
     * @param cacheName 缓存名称
     * @return 指标信息
     */
    CacheMetrics buildCacheMetrics(String cacheName);
}
