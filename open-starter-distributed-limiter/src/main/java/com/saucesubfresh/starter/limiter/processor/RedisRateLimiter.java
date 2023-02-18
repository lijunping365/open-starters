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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * redis + lua 脚本的方式实现，支持用户自定义 lua 脚本
 * 默认提供基于令牌桶的限流 lua 脚本，参考 spring-cloud-gateway</br>
 *
 * @author lijunping
 */
@Slf4j
public class RedisRateLimiter implements RateLimiter{

    private final RedisScript<List<Long>> script;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRateLimiter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        DefaultRedisScript script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/request_rate_limiter.lua")));
        script.setResultType(List.class);
        this.script = script;
    }

    @Override
    public <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity, int permits) {
        List<String> keys = getKeys(limitKey);
        List<Long> results = this.redisTemplate.execute(this.script, keys, rate, capacity, permits);

        if (!CollectionUtils.isEmpty(results) && results.get(0) == 1L) {
            if (log.isDebugEnabled()){
                log.debug("[限流统计],tokensLeft:{}", results.get(1));
            }
            return callback.get();
        }else {
            throw new LimitException("限流器限流了");
        }
    }

    /**
     * 注意：redisson RRateLimiter 的 keys 也是类似此结构
     * use `{}` around keys to use Redis Key hash tags
     * this allows for using redis cluster
     */
    static List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "request_rate_limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }
}
