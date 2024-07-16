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
package com.openbytecode.starter.cache.handler;

import com.openbytecode.starter.cache.exception.CacheExecuteException;
import com.openbytecode.starter.cache.message.CacheMessage;

/**
 * 监听缓存消息执行失败策略，可根据具体业务实现该接口，比如记录失败日志，失败重试等策略
 *
 * @author lijunping
 */
public interface CacheListenerErrorHandler {

    /**
     * 监听缓存消息执行失败策略
     *
     * @param exception 异常信息
     * @param cacheMessage 缓存消息
     */
    void onListenerError(CacheExecuteException exception, CacheMessage cacheMessage);
}
