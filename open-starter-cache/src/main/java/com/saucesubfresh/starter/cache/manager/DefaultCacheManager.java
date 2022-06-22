package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.ClusterCacheProvider;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 13:38
 */
public class DefaultCacheManager extends AbstractCacheManager {

    private static final String SAM = ":";
    private final RedissonClient client;
    private final CacheProperties properties;

    public DefaultCacheManager(CacheProperties properties,
                               ConfigFactory configFactory,
                               RedissonClient redissonClient) {
        super(configFactory);
        this.properties = properties;
        this.client = redissonClient;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        cacheName = generate(namespace, cacheName);
        return new ClusterCacheProvider(cacheName, client, cacheConfig);
    }

    protected String generate(String namespace, String cacheName){
        return namespace + SAM + cacheName;
    }
}
