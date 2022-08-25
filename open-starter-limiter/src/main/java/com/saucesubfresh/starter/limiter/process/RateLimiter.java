package com.saucesubfresh.starter.limiter.process;

import java.util.List;

/**
 * 限流
 * @author lijunping on 2022/8/25
 */
public interface RateLimiter {

    boolean tryAcquire(List<String> keys, int count);

    boolean tryAcquire(List<String> keys, int count, int period);

}
