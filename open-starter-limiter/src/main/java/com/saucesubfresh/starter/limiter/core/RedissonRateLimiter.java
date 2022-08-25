package com.saucesubfresh.starter.limiter.core;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

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
        rateLimiter.trySetRate(RateType.PER_CLIENT,5,2, RateIntervalUnit.MINUTES);
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void limit() {
        if(rateLimiter.tryAcquire()) {
            // todo
        }else {
            System.out.println("限流器限流了");
        }
    }
}
