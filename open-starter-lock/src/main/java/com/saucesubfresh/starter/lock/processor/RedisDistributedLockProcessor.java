package com.saucesubfresh.starter.lock.processor;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author lijunping on 2022/8/25
 */
public class RedisDistributedLockProcessor implements DistributedLockProcessor{

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisDistributedLockProcessor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public <T> T lock(Supplier<T> callback, String lockName, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        return null;
    }

    @Override
    public <T> T tryLock(Supplier<T> callback, String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        return null;
    }
}
