package com.saucesubfresh.starter.executor.interceptor;


import com.saucesubfresh.starter.executor.domain.Task;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认的任务的前置拦截器
 */
@Slf4j
public class DefaultTaskBeforeInterceptor implements TaskBeforeInterceptor {

  @Override
  public void before(Task task) throws Throwable {
    log.info("任务前置拦截器 {}", task);
  }
}
