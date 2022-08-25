package com.saucesubfresh.starter.limiter.process;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * 基于令牌桶算法
 *
 * @author lijunping on 2022/8/25
 */
@Slf4j
public class RedissonRateLimiter implements RateLimiter{

    private final RRateLimiter rateLimiter;

    public RedissonRateLimiter(RedissonClient client) {
        RRateLimiter rateLimiter = client.getRateLimiter("rate_limiter");
        rateLimiter.trySetRate(RateType.OVERALL,5,2, RateIntervalUnit.MINUTES);
        this.rateLimiter = rateLimiter;
    }


    @Override
    public boolean acquire(List<String> keys, int count) {
        return rateLimiter.tryAcquire();
    }

    @Override
    public boolean tryAcquire(List<String> keys, int count) {
        if(rateLimiter.tryAcquire()) {
            // todo
        }else {
            System.out.println("限流器限流了");
        }
    }

    @Override
    public boolean tryAcquire(List<String> keys, int count, int period) {
        return false;
    }
}
