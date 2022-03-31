package com.lightcode.starter.executor.executor;

import java.io.Closeable;
import java.util.concurrent.Executor;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务的执行器接口
 */
public interface TaskExecutor extends Executor, Closeable {

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
