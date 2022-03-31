package com.lightcode.starter.executor.component;


import com.lightcode.starter.executor.domain.Task;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行失败处理器
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
