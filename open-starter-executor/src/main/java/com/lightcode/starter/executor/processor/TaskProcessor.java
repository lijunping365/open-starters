package com.lightcode.starter.executor.processor;


import com.lightcode.starter.executor.domain.Task;
import com.lightcode.starter.executor.handler.TaskHandler;

import java.util.List;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行向外暴露接口
 */
public interface TaskProcessor<T extends Task> {

  /**
   * 任务执行方法
   *
   * @param tasks 要执行的任务
   * @param handler 任务执行回调函数
   */
  void execute(List<T> tasks, TaskHandler handler);

}
