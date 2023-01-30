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
import com.saucesubfresh.starter.cache.message.CacheMessage;

/**
 * 发送同步缓存消息失败策略，可根据具体业务实现该接口，比如记录失败日志，失败重试等策略
 *
 * @author lijunping on 2023/1/30
 */
public interface CacheProducerErrorHandler {

    /**
     * 发送同步缓存消息失败策略
     *
     * @param exception 异常信息
     * @param cacheMessage 缓存消息
     */
    void onProducerError(CacheBroadcastException exception, CacheMessage cacheMessage);
}
