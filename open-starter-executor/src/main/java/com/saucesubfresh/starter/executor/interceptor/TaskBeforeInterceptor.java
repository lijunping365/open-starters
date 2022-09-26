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
package com.saucesubfresh.starter.executor.interceptor;


import com.saucesubfresh.starter.executor.domain.Task;

/**
 * 任务的前拦截器
 *
 * @author lijunping
 */
public interface TaskBeforeInterceptor {

  /**
   * 在任务执行之前执行，如果出错请抛出 异常
   *
   * @param task task
   * @throws Throwable 前置请求出错的异常
   */
  default void before(Task task) throws Throwable {
  }

}
