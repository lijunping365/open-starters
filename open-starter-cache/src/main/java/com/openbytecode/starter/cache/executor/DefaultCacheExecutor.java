/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.cache.executor;

import com.openbytecode.starter.cache.core.ClusterCache;
import com.openbytecode.starter.cache.exception.CacheExecuteException;
import com.openbytecode.starter.cache.manager.CacheManager;
import com.openbytecode.starter.cache.message.CacheMessage;
import com.openbytecode.starter.cache.message.CacheCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultCacheExecutor extends AbstractCacheExecutor {

    public DefaultCacheExecutor(CacheManager cacheManager) {
        super(cacheManager);
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
                case UPDATE:
                    cache.put(key, value);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported Operation");
            }
        }catch (Exception e){
            throw new CacheExecuteException(e.getMessage(), e);
        }
    }
}
