package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.Cache;
import org.checkerframework.checker.units.qual.K;

import java.util.List;

/**
 * 提供用于管理后台操作的 api 接口
 * @author: 李俊平
 * @Date: 2022-05-29 13:36
 */
public interface CacheManager {
    /**
     * 获取缓存
     * @param namespace
     * @param cacheName
     * @return
     */
    Cache getCache(String namespace, String cacheName);
}
