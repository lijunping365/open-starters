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

import java.util.List;

/**
 * 任务执行器接口提供者
 *
 * @author lijunping
 */
public class DefaultTaskProcessor<T extends Task> extends AbstractTaskProcessor<T> {

  public DefaultTaskProcessor(TaskBuilder taskBuilder, ITaskExecutor executor) {
    super(taskBuilder, executor);
  }

  @Override
  protected void doExecute(List<Runnable> tasks, ITaskExecutor executor) {
    tasks.forEach(executor::execute);
  }

}
