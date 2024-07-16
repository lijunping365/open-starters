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

/**
 * 分布式锁处理器
 *
 * @author lijunping
 */

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁操作模板
 */
public interface DistributedLockProcessor {
  /**
   * 阻塞式分布式锁。自定义锁释放的超时时间
   *
   * @param callback   业务方法
   * @param lockName   分布式锁名称
   * @param leaseTime  锁超时时间。超时后自动释放锁。
   * @param timeUnit   时间单位
   * @param fairLock   是否使用公平锁
   * @param <T>
   * @return
   */
  <T> T lock(Supplier<T> callback, String lockName, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception;

  /**
   * 非阻塞式/阻塞式分布式锁，自定义等待时间、锁释放超时时间
   *
   * @param callback   业务方法
   * @param lockName   分布式锁名称
   * @param waitTime   获取锁最长等待时间
   * @param leaseTime  锁超时时间。超时后自动释放锁。
   * @param timeUnit   时间单位
   * @param fairLock   是否使用公平锁
   * @param <T>
   * @return
   */
  <T> T tryLock(Supplier<T> callback, String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception;
}
