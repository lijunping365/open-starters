/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.lock.processor;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 实现分布式锁模板
 *
 * @author lijunping
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
