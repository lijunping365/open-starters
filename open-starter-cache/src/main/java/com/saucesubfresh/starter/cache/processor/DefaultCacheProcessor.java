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
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheMessageProducer;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lijunping
 */
public class DefaultCacheProcessor extends AbstractCacheProcessor {

    private final CacheMessageProducer messageProducer;

    public DefaultCacheProcessor(CacheManager cacheManager, CacheMessageProducer messageProducer) {
        super(cacheManager);
        this.messageProducer = messageProducer;
    }

    @Override
    public Object handlerCacheable(Supplier<Object> callback, String cacheName, String cacheKey, Class<?> returnType) throws Throwable {
        Object value;
        final ClusterCache cache = super.getCache(cacheName);
        value = cache.get(cacheKey);
        if (Objects.nonNull(value)){
            return super.fromStoreValue(value);
        }
        value = callback.get();
        cache.put(cacheKey, value);

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.UPDATE)
                .key(cacheKey)
                .value(value)
                .build();
        publish(cacheMessage);
        return value;
    }

    @Override
    public void handlerCacheEvict(String cacheName, String cacheKey) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.evict(cacheKey);

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.INVALIDATE)
                .key(cacheKey)
                .build();
        publish(cacheMessage);
    }

    @Override
    public void handlerCacheClear(String cacheName) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.clear();

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.CLEAR)
                .build();
        publish(cacheMessage);
    }

    @Override
    public void handlerCachePut(String cacheName, String cacheKey, Object cacheValue) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.put(cacheKey, cacheValue);

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.UPDATE)
                .key(cacheKey)
                .value(cacheValue)
                .build();
        publish(cacheMessage);
    }

    /**
     * <p>
     *     发布同步缓存广播消息
     * </p>
     *
     * @param message 同步缓存消息
     */
    protected void publish(CacheMessage message){
        messageProducer.broadcastLocalCacheStore(message);
    }

}
