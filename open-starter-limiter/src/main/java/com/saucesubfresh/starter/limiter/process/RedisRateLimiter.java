package com.saucesubfresh.starter.limiter.process;

import com.saucesubfresh.starter.limiter.exception.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * redis + lua 脚本的方式实现，支持用户自定义 lua 脚本
 * 默认提供基于令牌桶的限流 lua 脚本，参考 spring-cloud-gateway
 *
 * @author lijunping on 2022/8/25
 */
@Slf4j
public class RedisRateLimiter implements RateLimiter{

    private final RedisScript<List<Long>> script;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRateLimiter(RedisScript<List<Long>> script,
                            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    @Override
    public <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity, int permits) {
        List<String> keys = getKeys(limitKey);

        // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
        List<Long> results = this.redisTemplate.execute(this.script, keys, rate, capacity, permits);

        boolean allowed = results.get(0) == 1L;
        Long tokensLeft = results.get(1);

        if (log.isDebugEnabled()){
            log.debug("[限流请求],limitKey:{}, permits:{}, allowed:{}, tokensLeft:{}", limitKey, permits, allowed, tokensLeft);
        }

        if (allowed) {
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
