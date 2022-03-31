package com.lightcode.starter.executor.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认线程池任务执行器
 */
@Slf4j
public class DefaultTaskExecutor implements TaskExecutor {

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
