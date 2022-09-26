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
 * 任务的后拦截器
 *
 * @author lijunping
 */
public interface TaskAfterInterceptor {

  /**
   * 任务执行之后的处理，任务可能成功，也可能失败
   * <p>
   * 如果可以的话，请尽量保证该方法是异步的
   *
   * @param task      task
   * @param throwable 失败时抛出的异常
   */
  default void after(Task task, Throwable throwable) {
  }

}
