package com.saucesubfresh.starter.cache.factory;

/**
 * @author lijunping on 2022/6/22
 */
public interface ConfigFactory {

    CacheConfig create(String cacheName);
}
