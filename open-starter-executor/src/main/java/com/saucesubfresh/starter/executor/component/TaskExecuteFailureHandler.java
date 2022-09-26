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
package com.saucesubfresh.starter.executor.component;


import com.saucesubfresh.starter.executor.domain.Task;

/**
 * 任务执行失败处理器
 *
 * @author lijunping
 */
public interface TaskExecuteFailureHandler {

  /**
   * 任务执行失败执行
   *
   * @param task 执行失败的任务
   * @param msg  任务执行失败原因
   */
  void onTaskExecuteFailure(Task task, String msg);
}
