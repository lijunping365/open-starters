package com.saucesubfresh.starter.executor.component;

import com.saucesubfresh.starter.executor.domain.Task;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认任务执行失败处理器
 */
@Slf4j
public class DefaultTaskExecuteFailureHandler implements TaskExecuteFailureHandler {
  @Override
  public void onTaskExecuteFailure(Task task, String msg) {
    log.info("任务执行失败了");
  }
}
