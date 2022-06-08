package com.saucesubfresh.starter.cache.service;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-06-08 23:39
 */
public interface CacheService {

    /**
     * 通过 namespace （相当于 applicationName）获取当前应用内所有的缓存
     * @param namespace 命名空间，区分应用
     * @return 所有缓存
     */
    List<String> getCacheNames(String namespace);

    /**
     * 缓存预热
     * @param namespace 命名空间，区分应用
     * @param cacheName 缓存名称
     * @param cacheKey 缓存 key
     * @return true： 预热成功，false ：预热失败
     */
    boolean preheat(String namespace, String cacheName, String cacheKey);

    /**
     * 清除某个 cacheName 下的所有缓存数据
     * @param namespace 命名空间，区分应用
     * @param cacheName 缓存名称
     * @return true： 清除成功，false ：清除失败
     */
    boolean clearCache(String namespace, String cacheName);

    /**
     * 清除某个 cacheName 下的某个 key 的缓存数据
     * @param namespace 命名空间，区分应用
     * @param cacheName 缓存名称
     * @return true： 清除成功，false ：清除失败
     */
    boolean evictCache(String namespace, String cacheName, String cacheKey);
}
