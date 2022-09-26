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
package com.saucesubfresh.starter.executor.processor;


import com.saucesubfresh.starter.executor.builder.TaskBuilder;
import com.saucesubfresh.starter.executor.domain.Task;
import com.saucesubfresh.starter.executor.executor.ITaskExecutor;
import com.saucesubfresh.starter.executor.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijunping
 */
@Slf4j
public abstract class AbstractTaskProcessor<T extends Task> implements TaskProcessor<T>{

  protected final TaskBuilder taskBuilder;
  protected final ITaskExecutor executor;

  protected AbstractTaskProcessor(TaskBuilder taskBuilder, ITaskExecutor executor) {
    this.taskBuilder = taskBuilder;
    this.executor = executor;
  }

  /**
   * 执行任务抽象方法，模板方法
   *
   * @param tasks 要执行的任务
   */
  @Override
  public void execute(List<T> tasks, TaskHandler handler) {
    // 1 构建任务
    List<Runnable> taskList = tasks.stream().map(e -> taskBuilder.build(e, handler)).collect(Collectors.toList());
    // 2 执行任务
    doExecute(taskList, executor);
  }

  protected abstract void doExecute(List<Runnable> tasks, ITaskExecutor executor);

}
