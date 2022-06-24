package com.saucesubfresh.starter.cache.metrics;

import java.util.List;

/**
 * @author lijunping on 2022/6/22
 */
public interface CacheMetricsPusher {

    void pushCacheMetrics(List<CacheMetrics> cacheMetrics);
}
