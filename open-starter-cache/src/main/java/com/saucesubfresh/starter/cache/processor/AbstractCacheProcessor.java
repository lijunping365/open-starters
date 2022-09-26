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
package com.saucesubfresh.starter.cache.processor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.domain.NullValue;
import com.saucesubfresh.starter.cache.manager.CacheManager;

/**
 * @author: 李俊平
 * @Date: 2022-06-16 23:26
 */
public abstract class AbstractCacheProcessor implements CacheProcessor {

    private final CacheManager cacheManager;

    public AbstractCacheProcessor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }

    protected Object fromStoreValue(Object storeValue) {
        if (storeValue == NullValue.INSTANCE) {
            return NullValue.INSTANCE.get();
        }
        return storeValue;
    }
}
