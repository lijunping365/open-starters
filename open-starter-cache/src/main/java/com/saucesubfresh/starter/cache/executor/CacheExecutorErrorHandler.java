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

import com.saucesubfresh.starter.cache.exception.CacheException;

/**
 * 执行失败策略
 *
 * @author lijunping
 */
public interface CacheExecutorErrorHandler {

    /**
     * 缓存操作执行失败后被调用
     *
     * @param cacheException 异常信息
     */
    void onExecuteError(CacheException cacheException);
}
