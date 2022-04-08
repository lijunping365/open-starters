package com.saucesubfresh.starter.executor.interceptor;


import com.saucesubfresh.starter.executor.domain.Task;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认的任务的后置拦截器
 */
@Slf4j
public class DefaultTaskAfterInterceptor implements TaskAfterInterceptor {

  @Override
  public void after(Task task, Throwable throwable) {
    log.info("任务后置拦截器 {}", task);
  }

}
