package com.saucesubfresh.starter.lock.processor;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author lijunping on 2022/8/25
 */
public class RedisDistributedLockProcessor implements DistributedLockProcessor{


    @Override
    public <T> T lock(Supplier<T> callback, String lockName, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        return null;
    }

    @Override
    public <T> T tryLock(Supplier<T> callback, String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        return null;
    }
}
