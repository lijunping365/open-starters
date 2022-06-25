package com.saucesubfresh.starter.cache.metrics;

import com.saucesubfresh.starter.cache.manager.CacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 12:00
 */
public class DefaultCacheMetricsCollector implements CacheMetricsCollector{

    private final CacheManager cacheManager;
    private final CacheMetricsBuilder cacheMetricsBuilder;

    public DefaultCacheMetricsCollector(CacheManager cacheManager, CacheMetricsBuilder cacheMetricsBuilder) {
        this.cacheManager = cacheManager;
        this.cacheMetricsBuilder = cacheMetricsBuilder;
    }

    @Override
    public List<CacheMetrics> collectCacheMetrics() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }
        return cacheNames.stream().map(cacheMetricsBuilder::buildCacheMetrics).collect(Collectors.toList());
    }
}
