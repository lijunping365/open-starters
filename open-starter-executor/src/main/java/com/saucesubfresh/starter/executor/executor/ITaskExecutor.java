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
package com.saucesubfresh.starter.executor.executor;

import java.io.Closeable;
import java.util.concurrent.Executor;

/**
 * 任务的执行器接口
 *
 * @author lijunping
 */
public interface ITaskExecutor extends Executor, Closeable {

  /**
   * Executes the given command at some time in the future.  The command
   * may execute in a new thread, in a pooled thread, or in the calling
   * thread, at the discretion of the {@code Executor} implementation.
   */
  @Override
  void execute(Runnable command);

  /**
   * 关闭执行器
   */
  @Override
  void close();

}
