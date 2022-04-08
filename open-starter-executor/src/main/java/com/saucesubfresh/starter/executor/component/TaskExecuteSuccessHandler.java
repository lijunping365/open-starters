package com.saucesubfresh.starter.executor.component;


import com.saucesubfresh.starter.executor.domain.Task;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行成功处理器
 */
public interface TaskExecuteSuccessHandler {

  void onTaskExecuteSuccess(Task task);
}
