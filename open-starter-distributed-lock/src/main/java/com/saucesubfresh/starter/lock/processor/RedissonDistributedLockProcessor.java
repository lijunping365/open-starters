package com.saucesubfresh.starter.lock.processor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 实现分布式锁模板
 *
 * @author: 李俊平
 * @Date: 2020-11-11 11:10
 */
public class RedissonDistributedLockProcessor implements DistributedLockProcessor {

  private final RedissonClient redisson;

  public RedissonDistributedLockProcessor(RedissonClient redisson) {
    this.redisson = redisson;
  }

  @Override
  public <T> T lock(Supplier<T> callback, String lockName, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception{
    RLock lock = getLock(lockName, fairLock);
    try {
      lock.lock(leaseTime, timeUnit);
      return callback.get();
    } finally {
      if (lock != null && lock.isLocked()) {
        lock.unlock();
      }
    }
  }

  @Override
  public <T> T tryLock(Supplier<T> callback, String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception{
    RLock lock = getLock(lockName, fairLock);
    try {
      if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
        return callback.get();
      }
    } finally {
      if (lock != null && lock.isLocked()) {
        lock.unlock();
      }
    }
    return null;
  }

  private RLock getLock(String lockName, boolean fairLock) {
    RLock lock;
    if (fairLock) {
      lock = redisson.getFairLock(lockName);
    } else {
      lock = redisson.getLock(lockName);
    }
    return lock;
  }
}
