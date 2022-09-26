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
package com.saucesubfresh.starter.schedule.config;


import com.saucesubfresh.starter.schedule.DefaultTaskJobScheduler;
import com.saucesubfresh.starter.schedule.TaskJobScheduler;
import com.saucesubfresh.starter.schedule.executor.DefaultScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.initializer.DefaultScheduleTaskInitializer;
import com.saucesubfresh.starter.schedule.initializer.ScheduleTaskInitializer;
import com.saucesubfresh.starter.schedule.loader.DefaultScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.loader.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.manager.HashedWheelScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.manager.LocalScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务执行器配置类
 *
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ScheduleProperties.class)
public class ScheduleTaskAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskPoolManager scheduleTaskManager(){
    return new LocalScheduleTaskPoolManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskQueueManager scheduleTaskQueueManager(ScheduleProperties scheduleProperties){
    return new HashedWheelScheduleTaskQueueManager(scheduleProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskLoader scheduleTaskLoader(){
    return new DefaultScheduleTaskLoader();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutor scheduleTaskExecutor(){
    return new DefaultScheduleTaskExecutor();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskJobScheduler taskJobScheduler(ScheduleTaskExecutor scheduleTaskExecutor,
                                           ScheduleTaskPoolManager scheduleTaskPoolManager,
                                           ScheduleTaskQueueManager scheduleTaskQueueManager){
    return new DefaultTaskJobScheduler(scheduleTaskExecutor, scheduleTaskPoolManager, scheduleTaskQueueManager);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskInitializer taskInitializer(TaskJobScheduler taskJobScheduler,
                                                 ScheduleTaskLoader scheduleTaskLoader,
                                                 ScheduleTaskPoolManager scheduleTaskPoolManager,
                                                 ScheduleTaskQueueManager scheduleTaskQueueManager){
    return new DefaultScheduleTaskInitializer(taskJobScheduler, scheduleTaskLoader, scheduleTaskPoolManager, scheduleTaskQueueManager);
  }
}
