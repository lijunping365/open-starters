package com.lightcode.starter.executor.handler;

import com.lightcode.starter.executor.domain.Task;
import com.lightcode.starter.executor.exception.TaskExecutorException;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行处理器,就这个任务到底要执行什么逻辑。
 */
@FunctionalInterface
public interface TaskHandler {

  /**
   * 任务处理器方法
   *
   * @param task
   */
  void handler(Task task) throws TaskExecutorException;
}
