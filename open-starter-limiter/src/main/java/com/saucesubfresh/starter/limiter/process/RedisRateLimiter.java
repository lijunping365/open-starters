package com.saucesubfresh.starter.limiter.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.List;

/**
 * 基于令牌桶算法
 *
 * @author lijunping on 2022/8/25
 */
@Slf4j
public class RedisRateLimiter implements RateLimiter{

    private final RedisScript<Long> script;
    private final StringRedisTemplate redisTemplate;


    public RedisRateLimiter(RedisScript<Long> script,
                            StringRedisTemplate redisTemplate) {
        this.script = script;
        this.redisTemplate = redisTemplate;

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
    public boolean acquire(List<String> keys, int count) {
        return false;
    }

    @Override
    public boolean tryAcquire(List<String> keys, int count) {
        final Long execute = this.redisTemplate.execute(this.script, keys, count);
        return false;
    }

    @Override
    public boolean tryAcquire(List<String> keys, int count, int period) {
        return false;
    }

    @Override
    public boolean tryAcquire(String keys, int count, int period) {
        return false;
    }
}
