package com.saucesubfresh.starter.cache.metrics;

import com.saucesubfresh.starter.cache.manager.CacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/6/24
 */
public abstract class AbstractCacheMetricsPusher implements CacheMetricsPusher {

    private final CacheManager cacheManager;

    private final CacheMetricsBuilder cacheMetricsBuilder;

    public AbstractCacheMetricsPusher(CacheManager cacheManager, CacheMetricsBuilder cacheMetricsBuilder) {
        this.cacheManager = cacheManager;
        this.cacheMetricsBuilder = cacheMetricsBuilder;
    }

    protected List<CacheMetrics> getCacheMetrics(){
        Collection<String> cacheNames = cacheManager.getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }
        return cacheNames.stream()
                .map(cacheMetricsBuilder::buildCacheMetrics)
                .collect(Collectors.toList());
    }
}
