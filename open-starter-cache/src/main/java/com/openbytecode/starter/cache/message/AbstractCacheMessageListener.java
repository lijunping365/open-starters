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
package com.openbytecode.starter.cache.message;

import com.openbytecode.starter.cache.exception.CacheExecuteException;
import com.openbytecode.starter.cache.executor.CacheExecutor;
import com.openbytecode.starter.cache.properties.CacheProperties;
import com.openbytecode.starter.cache.handler.CacheListenerErrorHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping
 */
public abstract class AbstractCacheMessageListener implements CacheMessageListener{

    private final CacheExecutor cacheExecutor;
    private final CacheProperties properties;
    private final CacheListenerErrorHandler errorHandler;

    protected AbstractCacheMessageListener(CacheExecutor cacheExecutor,
                                           CacheProperties properties,
                                           CacheListenerErrorHandler errorHandler) {
        this.cacheExecutor = cacheExecutor;
        this.properties = properties;
        this.errorHandler = errorHandler;
    }

    @Override
    public void onMessage(CacheMessage message) {
        String instanceId = properties.getInstanceId();
        if (StringUtils.equals(instanceId, message.getInstanceId())){
            return;
        }
        try {
            cacheExecutor.execute(message);
        }catch (CacheExecuteException e){
            errorHandler.onListenerError(e, message);
        }
    }
}
