/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.stats.ConcurrentStatsCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 注意点：
 * 1. 除了查询操作外，我们都是先执行二级缓存，然后再执行一级缓存
 *
 * 比如清除操作，先清除 redis 中缓存数据，然后再清除 caffeine 缓存，避免短时间内如果先清除 caffeine 缓存后其他请求会再从 redis 里加载到 caffeine 中
 *
 * @author lijunping
 */
@Slf4j
public class RedisCaffeineCache extends AbstractClusterCache {

    private final String cacheHashKey;
    private final Cache<Object, Object> cache;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCaffeineCache(String cacheName,
                              String namespace,
                              CacheConfig cacheConfig,
                              RedisTemplate<String, Object> redisTemplate) {
        super(new ConcurrentStatsCounter());
        this.cacheHashKey = super.generate(namespace, cacheName);
        this.redisTemplate = redisTemplate;
        this.cache = Caffeine.newBuilder()
                .maximumSize(cacheConfig.getMaxSize())
                .expireAfterWrite(cacheConfig.getTtl(), TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void preloadCache(int count) {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(count).build();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(cacheHashKey, scanOptions);
        Map<Object, Object> entries = new HashMap<>();
        while(cursor.hasNext()){
            entries.put(cursor.next().getKey(), cursor.next().getValue());
        }
        if (CollectionUtils.isEmpty(entries)){
            return;
        }
        entries.forEach(cache::put);
        cursor.close();
    }

    @Override
    public Object get(Object key) {
        Object value = cache.getIfPresent(key);
        if (value != null) {
            log.debug("get cache from caffeine, the key is : {}", key);
        } else {
            value = redisTemplate.opsForHash().get(cacheHashKey, key);
            if (value != null){
                cache.put(key, value);
            }
        }
        value = toValueWrapper(value);
        this.afterRead(value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        value = toStoreValue(value);
        redisTemplate.opsForHash().put(cacheHashKey, key, value);
        cache.put(key, value);
        this.afterPut();
    }

    @Override
    public void evict(Object key) {
        redisTemplate.opsForHash().delete(cacheHashKey, key);
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(cacheHashKey);
        cache.invalidateAll();
    }

    @Override
    public long getCacheKeyCount() {
        return redisTemplate.opsForHash().size(cacheHashKey);
    }

    @Override
    public Set<Object> getCacheKeySet(String pattern, int count) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(cacheHashKey, scanOptions);
        Set<Object> keySet = new HashSet<>();
        while(cursor.hasNext()){
            keySet.add(cursor.next().getKey());
        }
        cursor.close();
        return keySet;
    }
}
