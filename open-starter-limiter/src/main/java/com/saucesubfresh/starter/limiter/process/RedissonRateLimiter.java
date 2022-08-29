package com.saucesubfresh.starter.limiter.process;

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
 * @author lijunping on 2022/8/25
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
