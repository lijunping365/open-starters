package com.lightcode.starter.executor.builder;

import com.lightcode.starter.executor.domain.Task;
import com.lightcode.starter.executor.handler.TaskHandler;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务构建接口
 */
public interface TaskBuilder {

  /**
   * 构建任务方法
   *
   * @param task
   * @param handler
   * @return
   */
  Runnable build(Task task, TaskHandler handler);

}
