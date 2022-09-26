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

import com.saucesubfresh.starter.limiter.exception.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;

import java.util.function.Supplier;

/**
 * 使用 redisson 现成的限流实现 RRateLimiter，基于令牌桶算法
 * <pre>
 * 1、RateType.OVERALL 针对的是 RedissonClient 实例，如果只有一个 RedissonClient 实例，那么该参数设置哪个都没区别，
 * 如果有不止一个 RedissonClient 实例，那么 RateType.OVERALL 就是对于所有 RedissonClient 实例都生效，RateType.PER_CLIENT 只对当前单个实例生效
 * 2、默认生成 token 速度为每秒生成多少个
 * </pre>
 *
 * @author lijunping
 */
@Slf4j
public class RedissonRateLimiter implements RateLimiter{

    private final RedissonClient client;

    public RedissonRateLimiter(RedissonClient client) {
        this.client = client;
    }

    @Override
    public <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity, int permits) {
        RRateLimiter rateLimiter = client.getRateLimiter(limitKey);
        rateLimiter.trySetRate(RateType.PER_CLIENT, rate,1, RateIntervalUnit.SECONDS);
        if(rateLimiter.tryAcquire(permits)) {
            return callback.get();
        }else {
            throw new LimitException("限流器限流了");
        }
    }
}
