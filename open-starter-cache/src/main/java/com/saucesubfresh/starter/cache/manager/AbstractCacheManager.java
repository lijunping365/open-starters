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
package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lijunping
 */
@Slf4j
public abstract class AbstractCacheManager implements CacheManager{

    private final ConfigFactory configFactory;
    private final ConcurrentMap<String, ClusterCache> cacheMap = new ConcurrentHashMap<>(16);

    protected AbstractCacheManager(ConfigFactory configFactory) {
        this.configFactory = configFactory;
    }

    @Override
    public ClusterCache getCache(String cacheName) {
        return cacheMap.computeIfAbsent(cacheName, (t)->{
            CacheConfig cacheConfig = configFactory.create(cacheName);
            return this.createCache(cacheName, cacheConfig);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    protected abstract ClusterCache createCache(String cacheName, CacheConfig cacheConfig);
}
