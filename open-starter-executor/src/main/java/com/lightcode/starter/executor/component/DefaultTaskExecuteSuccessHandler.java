package com.lightcode.starter.executor.component;


import com.lightcode.starter.executor.domain.Task;
import lombok.extern.slf4j.Slf4j;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认任务执行成功处理器
 */
@Slf4j
public class DefaultTaskExecuteSuccessHandler implements TaskExecuteSuccessHandler {
  @Override
  public void onTaskExecuteSuccess(Task task) {
    log.info("任务执行成功了");
  }
}
