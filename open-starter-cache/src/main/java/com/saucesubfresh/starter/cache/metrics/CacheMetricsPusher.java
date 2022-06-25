package com.saucesubfresh.starter.cache.metrics;

import java.util.List;

/**
 * 缓存指标上报发送器
 *
 * @author lijunping on 2022/6/22
 */
public interface CacheMetricsPusher {

    /**
     * 上报缓存指标数据
     *
     * @param cacheMetrics 缓存指标信息
     */
    void pushCacheMetrics(List<CacheMetrics> cacheMetrics);
}
