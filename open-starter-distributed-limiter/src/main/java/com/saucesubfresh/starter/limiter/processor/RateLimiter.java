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
package com.saucesubfresh.starter.limiter.processor;

import java.util.function.Supplier;

/**
 * 限流接口
 *
 * @author lijunping
 */
public interface RateLimiter {

    /**
     * 尝试获取令牌
     * @param callback 业务回调方法
     * @param limitKey 限流 key
     * @param rate 令牌生成速率
     * @param <T> 业务返回类型
     * @return
     * @throws Exception
     */
    default <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate) throws Exception{
        return tryAcquire(callback, limitKey, rate, 100, 1);
    }

    /**
     * 尝试获取令牌
     * @param callback 业务回调方法
     * @param limitKey 限流 key
     * @param rate 令牌生成速率
     * @param capacity 令牌桶容量
     * @param <T> 业务返回类型
     * @return
     * @throws Exception
     */
    default <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity) throws Exception{
        return tryAcquire(callback, limitKey, rate, capacity, 1);
    }

    /**
     * 尝试获取令牌
     * @param callback 业务回调方法
     * @param limitKey 限流 key
     * @param rate 令牌生成速率
     * @param capacity 令牌桶容量
     * @param permits 获取令牌数量
     * @param <T> 业务返回类型
     * @return
     * @throws Exception
     */
    <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity, int permits) throws Exception;

}
