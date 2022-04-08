package com.saucesubfresh.starter.executor.builder;

import com.saucesubfresh.starter.executor.component.TaskExecuteFailureHandler;
import com.saucesubfresh.starter.executor.component.TaskExecuteSuccessHandler;
import com.saucesubfresh.starter.executor.domain.Task;
import com.saucesubfresh.starter.executor.handler.TaskHandler;
import com.saucesubfresh.starter.executor.interceptor.TaskAfterInterceptor;
import com.saucesubfresh.starter.executor.interceptor.TaskBeforeInterceptor;
import com.saucesubfresh.starter.executor.proxy.TaskProxy;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认的任务构建器
 */
public class DefaultTaskBuilder implements TaskBuilder {

  private final TaskExecuteSuccessHandler taskExecuteSuccessHandler;

  private final TaskExecuteFailureHandler taskExecuteFailureHandler;

  private final TaskBeforeInterceptor taskBeforeInterceptor;

  private final TaskAfterInterceptor taskAfterInterceptor;

  public DefaultTaskBuilder(TaskExecuteSuccessHandler taskExecuteSuccessHandler,
                            TaskExecuteFailureHandler taskExecuteFailureHandler,
                            TaskBeforeInterceptor taskBeforeInterceptor,
                            TaskAfterInterceptor taskAfterInterceptor) {
    this.taskExecuteSuccessHandler = taskExecuteSuccessHandler;
    this.taskExecuteFailureHandler = taskExecuteFailureHandler;
    this.taskBeforeInterceptor = taskBeforeInterceptor;
    this.taskAfterInterceptor = taskAfterInterceptor;
  }

  @Override
  public Runnable build(Task task, TaskHandler handler) {
    return proxy(task, () -> handler.handler(task));
  }

  /**
   * 使子类可以对 Runnable 进行代理，
   * 方法执行后返回 Runnable 代理
   *
   * @param task    当前的任务
   * @param command 任务
   * @return {@link Runnable} 返回包装或代理或者原来的 Runnable
   */
  protected Runnable proxy(Task task, Runnable command) {
    // 设置任务的上下文以及任务的拦截器
    return TaskProxy.proxy(task, command, taskExecuteSuccessHandler, taskExecuteFailureHandler, taskBeforeInterceptor, taskAfterInterceptor);
  }


}
