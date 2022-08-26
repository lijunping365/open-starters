package com.saucesubfresh.starter.limiter.process;

import java.util.List;
import java.util.function.Supplier;

/**
 * 限流
 * @author lijunping on 2022/8/25
 */
public interface RateLimiter {

    <T> T tryAcquire(Supplier<T> callback, String keys, int count);

    <T> T tryAcquire(Supplier<T> callback, String keys, int count, int period, double rate);

}
