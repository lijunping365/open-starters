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
import com.saucesubfresh.starter.schedule.annotation.EnableOpenScheduler;
import com.saucesubfresh.starter.schedule.executor.DefaultScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.initializer.DefaultScheduleTaskInitializer;
import com.saucesubfresh.starter.schedule.initializer.ScheduleTaskInitializer;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import com.saucesubfresh.starter.schedule.service.DefaultScheduleTaskService;
import com.saucesubfresh.starter.schedule.service.ScheduleTaskService;
import com.saucesubfresh.starter.schedule.trigger.DefaultScheduleTaskTrigger;
import com.saucesubfresh.starter.schedule.trigger.ScheduleTaskTrigger;
import com.saucesubfresh.starter.schedule.wheel.HashedTimeWheel;
import com.saucesubfresh.starter.schedule.wheel.TimeWheel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean(annotation = {EnableOpenScheduler.class})
public class ScheduleTaskAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskService scheduleTaskService(){
    return new DefaultScheduleTaskService();
  }

  @Bean
  @ConditionalOnMissingBean
  public TimeWheel timeWheel(ScheduleProperties scheduleProperties){
    return new HashedTimeWheel(scheduleProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutor scheduleTaskExecutor(){
    return new DefaultScheduleTaskExecutor();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskTrigger scheduleTaskTrigger(TimeWheel timeWheel, ScheduleTaskService scheduleTaskService, ScheduleTaskExecutor scheduleTaskExecutor){
    return new DefaultScheduleTaskTrigger(timeWheel, scheduleTaskService, scheduleTaskExecutor);
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskJobScheduler taskJobScheduler(ScheduleTaskTrigger scheduleTaskTrigger){
    return new DefaultTaskJobScheduler(scheduleTaskTrigger);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskInitializer scheduleTaskInitializer(TimeWheel timeWheel, ScheduleTaskService scheduleTaskService, TaskJobScheduler taskJobScheduler){
    return new DefaultScheduleTaskInitializer(timeWheel, scheduleTaskService, taskJobScheduler);
  }
}
