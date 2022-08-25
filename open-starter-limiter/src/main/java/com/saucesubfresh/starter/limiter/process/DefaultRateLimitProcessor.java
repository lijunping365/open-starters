package com.saucesubfresh.starter.limiter.process;

import com.saucesubfresh.starter.limiter.core.RateLimiter;

/**
 * @author lijunping on 2022/8/25
 */
public class DefaultRateLimitProcessor implements RateLimitProcessor{

    private final RateLimiter rateLimiter;

    public DefaultRateLimitProcessor(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean tryAcquire(String keys, int count) {
        return false;
    }

    @Override
    public boolean tryAcquire(String keys, int count, int period) {
        return false;
    }
}
