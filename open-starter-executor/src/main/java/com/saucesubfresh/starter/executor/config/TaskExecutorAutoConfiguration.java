/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.executor.config;


import com.saucesubfresh.starter.executor.builder.DefaultTaskBuilder;
import com.saucesubfresh.starter.executor.builder.TaskBuilder;
import com.saucesubfresh.starter.executor.component.DefaultTaskExecuteFailureHandler;
import com.saucesubfresh.starter.executor.component.DefaultTaskExecuteSuccessHandler;
import com.saucesubfresh.starter.executor.component.TaskExecuteFailureHandler;
import com.saucesubfresh.starter.executor.component.TaskExecuteSuccessHandler;
import com.saucesubfresh.starter.executor.executor.DefaultTaskExecutor;
import com.saucesubfresh.starter.executor.executor.ITaskExecutor;
import com.saucesubfresh.starter.executor.interceptor.DefaultTaskAfterInterceptor;
import com.saucesubfresh.starter.executor.interceptor.DefaultTaskBeforeInterceptor;
import com.saucesubfresh.starter.executor.interceptor.TaskAfterInterceptor;
import com.saucesubfresh.starter.executor.interceptor.TaskBeforeInterceptor;
import com.saucesubfresh.starter.executor.processor.DefaultTaskProcessor;
import com.saucesubfresh.starter.executor.processor.TaskProcessor;
import com.saucesubfresh.starter.executor.properties.TaskExecutorProperties;
import com.saucesubfresh.starter.executor.thread.ResizableCapacityLinkedBlockingQueue;
import com.saucesubfresh.starter.executor.thread.TaskThreadPoolExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务执行器配置类
 *
 * @author lijunping
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
