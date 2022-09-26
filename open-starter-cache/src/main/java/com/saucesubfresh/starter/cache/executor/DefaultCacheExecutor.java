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
package com.saucesubfresh.starter.cache.executor;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping on 2022/6/16
 */
@Slf4j
public class DefaultCacheExecutor extends AbstractCacheExecutor {

    public DefaultCacheExecutor(CacheManager cacheManager, CacheExecutorErrorHandler errorHandler) {
        super(cacheManager, errorHandler);
    }

    @Override
    public void execute(CacheMessage message){
        CacheCommand command = message.getCommand();
        String cacheName = message.getCacheName();
        Object key = message.getKey();
        Object value = message.getValue();
        ClusterCache cache = super.getCache(cacheName);
        try {
            switch (command){
                case CLEAR:
                    cache.clear();
                    break;
                case INVALIDATE:
                    cache.evict(key);
                    break;
                case PRELOAD:
                    cache.preloadCache();
                    break;
                case UPDATE:
                    cache.put(key, value);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported Operation");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            onError(e.getMessage(), message);
        }
    }
}
