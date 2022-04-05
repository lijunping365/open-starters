package com.lightcode.starter.executor.config;


import com.lightcode.starter.executor.builder.DefaultTaskBuilder;
import com.lightcode.starter.executor.builder.TaskBuilder;
import com.lightcode.starter.executor.component.DefaultTaskExecuteFailureHandler;
import com.lightcode.starter.executor.component.DefaultTaskExecuteSuccessHandler;
import com.lightcode.starter.executor.component.TaskExecuteFailureHandler;
import com.lightcode.starter.executor.component.TaskExecuteSuccessHandler;
import com.lightcode.starter.executor.executor.DefaultTaskExecutor;
import com.lightcode.starter.executor.executor.ITaskExecutor;
import com.lightcode.starter.executor.interceptor.DefaultTaskAfterInterceptor;
import com.lightcode.starter.executor.interceptor.DefaultTaskBeforeInterceptor;
import com.lightcode.starter.executor.interceptor.TaskAfterInterceptor;
import com.lightcode.starter.executor.interceptor.TaskBeforeInterceptor;
import com.lightcode.starter.executor.processor.DefaultTaskProcessor;
import com.lightcode.starter.executor.processor.TaskProcessor;
import com.lightcode.starter.executor.properties.TaskExecutorProperties;
import com.lightcode.starter.executor.thread.ResizableCapacityLinkedBlockingQueue;
import com.lightcode.starter.executor.thread.TaskThreadPoolExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 任务执行器配置类
 */
@Configuration
@EnableConfigurationProperties(TaskExecutorProperties.class)
public class TaskExecutorAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ThreadPoolExecutor threadPoolExecutor(TaskExecutorProperties properties,
                                               ResizableCapacityLinkedBlockingQueue<Runnable> blockingQueue){
    return new TaskThreadPoolExecutor(properties, blockingQueue);
  }

  @Bean
  @ConditionalOnMissingBean
  public ResizableCapacityLinkedBlockingQueue<Runnable> blockingQueue(TaskExecutorProperties properties){
    return new ResizableCapacityLinkedBlockingQueue<>(properties.getQueueCapacity());
  }

  @Bean
  @ConditionalOnMissingBean
  public ITaskExecutor iTaskExecutor(ThreadPoolExecutor threadPoolExecutor) {
    return new DefaultTaskExecutor(threadPoolExecutor);
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskBuilder taskBuilder(TaskExecuteSuccessHandler taskExecuteSuccessHandler,
                                 TaskExecuteFailureHandler taskExecuteFailureHandler,
                                 TaskBeforeInterceptor taskBeforeInterceptor,
                                 TaskAfterInterceptor taskAfterInterceptor) {
    return new DefaultTaskBuilder(taskExecuteSuccessHandler, taskExecuteFailureHandler, taskBeforeInterceptor, taskAfterInterceptor);
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskProcessor<?> taskProcessor(TaskBuilder taskBuilder, ITaskExecutor iTaskExecutor){
    return new DefaultTaskProcessor<>(taskBuilder, iTaskExecutor);
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskExecuteSuccessHandler taskExecuteSuccessHandler() {
    return new DefaultTaskExecuteSuccessHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskExecuteFailureHandler taskExecuteFailureHandler() {
    return new DefaultTaskExecuteFailureHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskBeforeInterceptor taskBeforeInterceptor() {
    return new DefaultTaskBeforeInterceptor();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskAfterInterceptor taskAfterInterceptor() {
    return new DefaultTaskAfterInterceptor();
  }

}
