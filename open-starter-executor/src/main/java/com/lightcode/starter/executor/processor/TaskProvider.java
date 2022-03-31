package com.lightcode.starter.executor.processor;

import com.lightcode.starter.executor.builder.TaskBuilder;
import com.lightcode.starter.executor.domain.Task;
import com.lightcode.starter.executor.executor.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行器接口提供者
 */
@Component
public class TaskProvider<T extends Task> extends AbstractTaskProcessor<T> {

  protected TaskProvider(TaskBuilder taskBuilder, TaskExecutor executor) {
    super(taskBuilder, executor);
  }

  @Override
  protected void doExecute(List<Runnable> tasks, TaskExecutor executor) {
    tasks.forEach(executor::execute);
  }

}
