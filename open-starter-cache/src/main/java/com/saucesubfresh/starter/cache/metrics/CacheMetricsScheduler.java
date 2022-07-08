package com.saucesubfresh.starter.cache.metrics;

/**
 * 缓存指标信息上报触发器
 *
 * @author: 李俊平
 * @Date: 2022-06-25 12:07
 */
public interface CacheMetricsScheduler {

    /**
     * 触发缓存指标信息上报
     */
    void triggerPushMetrics();
}
