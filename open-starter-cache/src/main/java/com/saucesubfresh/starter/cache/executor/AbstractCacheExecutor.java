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
import com.saucesubfresh.starter.cache.exception.CacheException;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheMessage;

/**
 * @author lijunping on 2022/6/16
 */
public abstract class AbstractCacheExecutor implements CacheExecutor {

    private final CacheManager cacheManager;
    private final CacheExecutorErrorHandler errorHandler;

    protected AbstractCacheExecutor(CacheManager cacheManager, CacheExecutorErrorHandler errorHandler) {
        this.cacheManager = cacheManager;
        this.errorHandler = errorHandler;
    }

    protected ClusterCache getCache(String cacheName){
        return cacheManager.getCache(cacheName);
    }

    protected void onError(String errMsg, CacheMessage message){
        CacheException cacheException = new CacheException(errMsg, message);
        errorHandler.onExecuteError(cacheException);
    }
}
