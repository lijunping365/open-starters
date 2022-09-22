package com.saucesubfresh.starter.lock.processor;

/**
 * 分布式锁处理器
 *
 * @author: 李俊平
 * @Date: 2020-11-11 11:09
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
