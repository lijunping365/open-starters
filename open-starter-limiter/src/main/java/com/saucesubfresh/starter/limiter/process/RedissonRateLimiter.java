package com.saucesubfresh.starter.limiter.process;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.function.Supplier;

/**
 * 基于令牌桶算法
 *
 * @author lijunping on 2022/8/25
 */
@Slf4j
public class RedissonRateLimiter implements RateLimiter{

    private final RedissonClient client;

    public RedissonRateLimiter(RedissonClient client) {
        this.client = client;
    }

    @Override
    public <T> T tryAcquire(Supplier<T> callback, String keys, int count) {
        return null;
    }

    @Override
    public <T> T tryAcquire(Supplier<T> callback, String keys, int count, int period, double rate) {
        RRateLimiter rateLimiter = client.getRateLimiter(keys);
        rateLimiter.trySetRate(RateType.OVERALL,5,2, RateIntervalUnit.SECONDS);
        if(rateLimiter.tryAcquire()) {
            return callback.get();
        }else {
            System.out.println("限流器限流了");
        }
        return null;
    }
}
