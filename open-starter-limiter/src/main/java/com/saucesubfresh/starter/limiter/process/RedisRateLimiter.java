package com.saucesubfresh.starter.limiter.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 基于令牌桶算法
 *
 * @author lijunping on 2022/8/25
 */
@Slf4j
public class RedisRateLimiter implements RateLimiter{

    private final RedisScript<Long> script;
    private final RedisTemplate<String, Object> redisTemplate;


    public RedisRateLimiter(RedisScript<Long> script,
                            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

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


    @Override
    public <T> T tryAcquire(Supplier<T> callback, String keys, int count) {
        return null;
    }

    @Override
    public <T> T tryAcquire(Supplier<T> callback, String keys, int permits, int period, double rate) {
        Long count = this.redisTemplate.execute(this.script, Collections.singletonList(keys), permits);
        log.info("[令牌桶限流交易请求],key:{},返回:{},{}秒内,限制次数:{}",keys, permits, period, rate);
        if (count != null && count.intValue() == 1) {
            return callback.get();
        }
        return null;
    }
}
