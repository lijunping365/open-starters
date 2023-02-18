/*
 * Copyright © 2022 organization SauceSubFresh
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
package com.saucesubfresh.starter.lock.annotation;

import com.saucesubfresh.starter.lock.generator.SimpleKeyGenerator;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于 redisson 的分布式锁的注解实现
 *
 * @author lijunping
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

  /**
   * 锁名称 {@link SimpleKeyGenerator}
   */
  String lockName() default "";

  /**
   * 是否使用公平锁，公平锁即先来先得。
   */
  boolean fairLock() default false;

  /**
   * 是否使用尝试锁。超过等待时间不会在阻塞
   */
  boolean tryLock() default false;

  /**
   * 最长等待时间。该字段只有当tryLock()返回true才有效。默认为 -1
   */
  long waitTime() default -1;

  /**
   * 锁超时时间。超时时间过后，锁自动释放。
   * 默认为 -1，即使用 redisson 默认的 30s
   * 注意：指定值为 -1 时看门狗才会工作
   */
  long leaseTime() default -1;

  /**
   * 时间单位。默认为毫秒。
   */
  TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
