package com.lightcode.starter.executor.interceptor;


import com.lightcode.starter.executor.domain.Task;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务的前拦截器
 */
public interface TaskBeforeInterceptor {

  /**
   * 在任务执行之前执行，如果出错请抛出 异常
   *
   * @param task task
   * @throws Throwable 前置请求出错的异常
   */
  default void before(Task task) throws Throwable {
  }

}
