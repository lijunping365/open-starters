package com.saucesubfresh.starter.executor.interceptor;


import com.saucesubfresh.starter.executor.domain.Task;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务的后拦截器
 */
public interface TaskAfterInterceptor {

  /**
   * 任务执行之后的处理，任务可能成功，也可能失败
   * <p>
   * 如果可以的话，请尽量保证该方法是异步的
   *
   * @param task      task
   * @param throwable 失败时抛出的异常
   */
  default void after(Task task, Throwable throwable) {
  }

}
