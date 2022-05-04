package com.saucesubfresh.starter.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于 redisson 的分布式锁的注解实现
 *
 * @author: 李俊平
 * @Date: 2020-11-11 09:59
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
  /**
   * 锁名称的前缀。
   */
  String prefix() default "";

  /**
   * Spring Expression Language (SpEL) expression for computing the key dynamically.
   * <p>Default is {@code ""}, meaning all method parameters are considered as a key,
   * <p>The SpEL expression evaluates against a dedicated context that provides the
   * following meta-data:
   * <ul>
   * <li>{@code #root.method}, {@code #root.target}, and {@code #root.lock} for
   * references to the {@link java.lang.reflect.Method method}, target object, and
   * affected cache(s) respectively.</li>
   * <li>Shortcuts for the method name ({@code #root.methodName}) and target class
   * ({@code #root.targetClass}) are also available.
   * <li>Method arguments can be accessed by index. For instance the second argument
   * can be accessed via {@code #root.args[1]}, {@code #p1} or {@code #a1}. Arguments
   * can also be accessed by name if that information is available.</li>
   * </ul>
   */
  String key() default "";

  /**
   * 是否使用公平锁。
   * 公平锁即先来先得。
   */
  boolean fairLock() default false;

  /**
   * 是否使用尝试锁。
   */
  boolean tryLock() default false;

  /**
   * 最长等待时间。
   * 该字段只有当tryLock()返回true才有效。
   */
  long waitTime() default 30L;

  /**
   * 锁超时时间。
   * 超时时间过后，锁自动释放。
   * 建议：尽量缩简需要加锁的逻辑。
   */
  long leaseTime() default 500L;

  /**
   * 时间单位。默认为毫秒。
   */
  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
