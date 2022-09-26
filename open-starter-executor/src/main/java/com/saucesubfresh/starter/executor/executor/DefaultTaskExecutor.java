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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认线程池任务执行器
 *
 * @author lijunping
 */
@Slf4j
public class DefaultTaskExecutor implements ITaskExecutor {

  private final ThreadPoolExecutor executor;

  public DefaultTaskExecutor(ThreadPoolExecutor threadPoolExecutor) {
    executor = threadPoolExecutor;
  }

  @Override
  public void close() {
    log.info("即将关闭线程池，会等队列中的任务完全执行之后才会关闭线程池");
    executor.shutdown();
  }

  @Override
  public void execute(Runnable command) {
    executor.execute(command);
  }

}
