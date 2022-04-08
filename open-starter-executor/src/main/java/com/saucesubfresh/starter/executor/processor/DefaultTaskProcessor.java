package com.saucesubfresh.starter.executor.processor;

import com.saucesubfresh.starter.executor.builder.TaskBuilder;
import com.saucesubfresh.starter.executor.domain.Task;
import com.saucesubfresh.starter.executor.executor.ITaskExecutor;

import java.util.List;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行器接口提供者
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
