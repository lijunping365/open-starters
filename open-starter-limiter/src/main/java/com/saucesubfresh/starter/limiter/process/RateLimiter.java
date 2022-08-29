package com.saucesubfresh.starter.limiter.process;

import java.util.function.Supplier;

/**
 * 限流
 * @author lijunping on 2022/8/25
 */
public interface RateLimiter {

    /**
     *
     * @param callback
     * @param limitKey
     * @param rate
     * @param <T>
     * @return
     * @throws Exception
     */
    default <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate) throws Exception{
        return tryAcquire(callback, limitKey, rate, 100, 1);
    }

    /**
     *
     * @param callback
     * @param limitKey
     * @param rate
     * @param capacity
     * @param <T>
     * @return
     * @throws Exception
     */
    default <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity) throws Exception{
        return tryAcquire(callback, limitKey, rate, capacity, 1);
    }

    /**
     *
     * @param callback
     * @param limitKey
     * @param rate
     * @param capacity
     * @param permits
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity, int permits) throws Exception;

}
