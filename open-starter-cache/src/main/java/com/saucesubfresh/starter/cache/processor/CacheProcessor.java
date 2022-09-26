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

import java.util.function.Supplier;

/**
 * 缓存注解处理器
 *
 * @author lijunping on 2022/5/25
 */
public interface CacheProcessor {

    Object handlerCacheable(Supplier<Object> callback, String cacheName, String cacheKey) throws Throwable;

    void handlerCacheEvict(String cacheName, String cacheKey) throws Throwable;

    void handlerCacheClear(String cacheName) throws Throwable;

    void handlerCachePut(String cacheName, String cacheKey, Object cacheValue) throws Throwable;
}
