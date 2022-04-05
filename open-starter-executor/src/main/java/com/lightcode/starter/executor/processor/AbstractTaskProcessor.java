package com.lightcode.starter.executor.processor;


import com.lightcode.starter.executor.builder.TaskBuilder;
import com.lightcode.starter.executor.domain.Task;
import com.lightcode.starter.executor.executor.ITaskExecutor;
import com.lightcode.starter.executor.handler.TaskHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:
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
