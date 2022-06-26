package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.RedissonCaffeineCache;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.message.CacheMessageProducer;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 13:38
 */
public class RedissonCaffeineCacheManager extends AbstractCacheManager {

    private final RedissonClient client;
    private final CacheProperties properties;
    private final CacheMessageProducer producer;

    public RedissonCaffeineCacheManager(CacheProperties properties,
                                        ConfigFactory configFactory,
                                        RedissonClient redissonClient,
                                        CacheMessageProducer producer) {
        super(configFactory);
        this.properties = properties;
        this.client = redissonClient;
        this.producer = producer;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        cacheName = generate(namespace, cacheName);
        return new RedissonCaffeineCache(cacheName, cacheConfig, client, producer);
    }
}
