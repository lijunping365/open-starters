package com.saucesubfresh.starter.limiter.process;

import java.util.function.Supplier;

/**
 * 限流
 * @author lijunping on 2022/8/25
 */
public interface RateLimiter {

    /**
     * 尝试获取令牌
     * @param callback 业务回调方法
     * @param limitKey 限流 key
     * @param rate 令牌生成速率
     * @param <T> 业务返回类型
     * @return
     * @throws Exception
     */
    default <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate) throws Exception{
        return tryAcquire(callback, limitKey, rate, 100, 1);
    }

    /**
     * 尝试获取令牌
     * @param callback 业务回调方法
     * @param limitKey 限流 key
     * @param rate 令牌生成速率
     * @param capacity 令牌桶容量
     * @param <T> 业务返回类型
     * @return
     * @throws Exception
     */
    default <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity) throws Exception{
        return tryAcquire(callback, limitKey, rate, capacity, 1);
    }

    /**
     * 尝试获取令牌
     * @param callback 业务回调方法
     * @param limitKey 限流 key
     * @param rate 令牌生成速率
     * @param capacity 令牌桶容量
     * @param permits 获取令牌数量
     * @param <T> 业务返回类型
     * @return
     * @throws Exception
     */
    <T> T tryAcquire(Supplier<T> callback, String limitKey, int rate, int capacity, int permits) throws Exception;

}
