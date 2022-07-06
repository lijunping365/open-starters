package com.saucesubfresh.starter.schedule.config;


import com.saucesubfresh.starter.schedule.core.ScheduleTaskManage;
import com.saucesubfresh.starter.schedule.core.RedisScheduleTaskManage;
import com.saucesubfresh.starter.schedule.executor.DefaultScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import com.saucesubfresh.starter.schedule.service.DefaultScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.service.ScheduleTaskLoader;
import com.saucesubfresh.starter.schedule.thread.TaskThreadPoolExecutor;
import com.saucesubfresh.starter.schedule.trigger.DefaultScheduleTaskTrigger;
import com.saucesubfresh.starter.schedule.trigger.ScheduleTaskTrigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 定时任务执行器配置类
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ScheduleProperties.class)
public class ScheduleTaskAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(RedisTemplate.class)
  public ScheduleTaskManage scheduleTaskManage(ScheduleProperties scheduleProperties,
                                               ScheduleTaskLoader scheduleTaskLoader,
                                               RedisTemplate<String, Object> redisTemplate){
    return new RedisScheduleTaskManage(scheduleProperties, scheduleTaskLoader, redisTemplate);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutor scheduleTaskExecutor(){
    return new DefaultScheduleTaskExecutor();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskTrigger scheduleTaskTrigger(ThreadPoolExecutor executor,
                                                 ScheduleTaskManage scheduleTaskManage,
                                                 ScheduleTaskExecutor scheduleTaskExecutor){
    return new DefaultScheduleTaskTrigger(executor, scheduleTaskManage, scheduleTaskExecutor);
  }

  @Bean
  @ConditionalOnMissingBean
  public ThreadPoolExecutor threadPoolExecutor(ScheduleProperties properties){
    return new TaskThreadPoolExecutor(properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskLoader scheduleTaskLoader(){
    return new DefaultScheduleTaskLoader();
  }
}
