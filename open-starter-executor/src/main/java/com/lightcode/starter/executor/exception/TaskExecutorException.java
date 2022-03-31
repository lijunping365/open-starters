package com.lightcode.starter.executor.exception;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行异常类
 */
public class TaskExecutorException extends RuntimeException {

  private static final long serialVersionUID = -4751489831503580980L;

  public TaskExecutorException(String msg) {
    super(msg);
  }
}
