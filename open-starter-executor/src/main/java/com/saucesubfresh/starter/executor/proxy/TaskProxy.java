package com.saucesubfresh.starter.executor.proxy;


import com.saucesubfresh.starter.executor.component.TaskExecuteFailureHandler;
import com.saucesubfresh.starter.executor.component.TaskExecuteSuccessHandler;
import com.saucesubfresh.starter.executor.domain.Task;
import com.saucesubfresh.starter.executor.interceptor.TaskAfterInterceptor;
import com.saucesubfresh.starter.executor.interceptor.TaskBeforeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 使用动态代理将拦截器加入到任务执行的前后
 */
@Slf4j
public class TaskProxy implements InvocationHandler, TaskDecorator {

  private final Task task;

  private final Runnable command;

  private final TaskExecuteSuccessHandler taskExecuteSuccessHandler;

  private final TaskExecuteFailureHandler taskExecuteFailureHandler;

  private final TaskBeforeInterceptor taskBeforeInterceptor;

  private final TaskAfterInterceptor taskAfterInterceptor;

  public TaskProxy(Task task, Runnable command, TaskExecuteSuccessHandler taskExecuteSuccessHandler, TaskExecuteFailureHandler taskExecuteFailureHandler, TaskBeforeInterceptor taskBeforeInterceptor, TaskAfterInterceptor taskAfterInterceptor) {
    this.task = task;
    this.command = command;
    this.taskExecuteSuccessHandler = taskExecuteSuccessHandler;
    this.taskExecuteFailureHandler = taskExecuteFailureHandler;
    this.taskBeforeInterceptor = taskBeforeInterceptor;
    this.taskAfterInterceptor = taskAfterInterceptor;
  }

  public static Runnable proxy(Task task, Runnable command, TaskExecuteSuccessHandler taskExecuteSuccessHandler, TaskExecuteFailureHandler taskExecuteFailureHandler, TaskBeforeInterceptor taskBeforeInterceptor, TaskAfterInterceptor taskAfterInterceptor) {
    TaskProxy proxy = new TaskProxy(task, command, taskExecuteSuccessHandler, taskExecuteFailureHandler, taskBeforeInterceptor, taskAfterInterceptor);
    return proxy.decorate(command);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) {

    Throwable throwable = null;

    Object result = null;

    try {
      onBefore();

      result = method.invoke(command, args);

      onSuccess();
    } catch (Throwable t) {
      throwable = t;
      onError(throwable);
    } finally {
      onComplete(throwable);
    }
    return result;
  }

  protected void onBefore() throws Throwable {
    taskBeforeInterceptor.before(task);
  }

  protected void onSuccess() {
    taskExecuteSuccessHandler.onTaskExecuteSuccess(task);
  }

  protected void onError(Throwable throwable) {
    taskExecuteFailureHandler.onTaskExecuteFailure(task, throwable.getMessage());
  }

  protected void onComplete(Throwable throwable) {
    taskAfterInterceptor.after(task, throwable);
  }

  @Override
  public Runnable decorate(Runnable command) {
    return (Runnable) Proxy.newProxyInstance(
        command.getClass().getClassLoader(),
        command.getClass().getInterfaces(),
        this
    );
  }
}
