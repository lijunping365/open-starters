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
package com.saucesubfresh.starter.cache.handler;

import com.saucesubfresh.starter.cache.exception.CacheBroadcastException;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijunping on 2023/1/30
 */
@Slf4j
public class DefaultCacheProducerErrorHandler implements CacheProducerErrorHandler{

    @Override
    public void onProducerError(CacheBroadcastException exception, CacheMessage message) {
        final String cacheName = message.getCacheName();
        CacheCommand command = message.getCommand();
        log.error("发送缓存同步消息异常 = {}，cacheName = {}, 异常原因 = {}", command.name(), cacheName, message);
    }
}
