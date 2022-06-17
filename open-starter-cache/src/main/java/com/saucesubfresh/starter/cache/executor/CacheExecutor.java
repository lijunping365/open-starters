package com.saucesubfresh.starter.cache.executor;

/**
 * 主要功能： 监听外部命令，对缓存进行操作
 * @author lijunping on 2022/6/16
 */
public interface CacheExecutor {

    /**
     * 缓存预热，远程数据同步到本地
     * @param cacheName 缓存名称
     * @param cacheKey 缓存 key
     */
    void preloadCache(String cacheName, String cacheKey);

    /**
     * 清除某个 cacheName 下的所有缓存数据
     * @param cacheName 缓存名称
     */
    void clearCache(String cacheName);

    /**
     * 清除某个 cacheName 下的某个 key 的缓存数据
     * @param cacheName 缓存名称
     */
    void evictCache(String cacheName, String cacheKey);
}
