package com.saucesubfresh.starter.cache.metrics;

import java.util.List;

/**
 * CacheMetrics 收集器
 *
 * @author: 李俊平
 * @Date: 2022-06-25 11:58
 */
public interface CacheMetricsCollector {

    /**
     * 搜集所有 cacheName 并返回集合 {@link CacheMetrics}
     *
     * @return List<CacheMetrics>
     */
    List<CacheMetrics> collectCacheMetrics();
}
