package com.saucesubfresh.starter.cache.service;

import com.saucesubfresh.starter.cache.domain.CacheStatsInfo;

import java.util.List;

/**
 * 主要功能： 供外部（admin）调用，获取缓存的一些指标，统计数据等，不会对缓存进行操作
 * @author: 李俊平
 * @Date: 2022-06-08 23:39
 */
public interface CacheStatsService {

    /**
     * 通过 namespace （相当于 applicationName）获取当前应用内所有的缓存
     * @return 所有缓存
     */
    List<CacheStatsInfo> getCacheStatsInfo();

}
