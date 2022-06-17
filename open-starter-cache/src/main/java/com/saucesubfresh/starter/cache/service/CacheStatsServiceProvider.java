package com.saucesubfresh.starter.cache.service;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.domain.CacheStatsInfo;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.properties.CacheConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: 李俊平
 * @Date: 2022-06-08 23:39
 */
public class CacheStatsServiceProvider implements CacheStatsService {

    private final CacheManager cacheManager;

    public CacheStatsServiceProvider(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public List<CacheStatsInfo> getCacheStatsInfo() {
        final Map<String, CacheConfig> cacheConfigList = cacheManager.getCacheConfigList();
        final Map<String, ClusterCache> cacheList = cacheManager.getCacheList();

        List<CacheStatsInfo> cacheStatsInfos = new ArrayList<>();
        cacheConfigList.forEach((key, value)->{
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            final ClusterCache clusterCache = cacheList.get(key);

            cacheStatsInfos.add(cacheStatsInfo);
        });

        return cacheStatsInfos;
    }
}
