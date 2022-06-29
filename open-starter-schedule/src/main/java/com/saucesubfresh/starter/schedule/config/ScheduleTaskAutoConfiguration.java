package com.saucesubfresh.starter.schedule.config;


import com.saucesubfresh.starter.schedule.core.ScheduleTaskManage;
import com.saucesubfresh.starter.schedule.core.support.RedisScheduleTaskManage;
import com.saucesubfresh.starter.schedule.executor.DefaultScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.trigger.DefaultScheduleTaskTrigger;
import com.saucesubfresh.starter.schedule.trigger.ScheduleTaskTrigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 定时任务执行器配置类
 */
@Configuration(proxyBeanMethods = false)
public class ScheduleTaskAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(RedisTemplate.class)
  public ScheduleTaskManage scheduleTaskManage(RedisTemplate<String, Object> redisTemplate){
    return new RedisScheduleTaskManage(redisTemplate);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutor scheduleTaskExecutor(){
    return new DefaultScheduleTaskExecutor();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskTrigger scheduleTaskTrigger(ScheduleTaskManage scheduleTaskManage, ScheduleTaskExecutor scheduleTaskExecutor){
    return new DefaultScheduleTaskTrigger(scheduleTaskManage, scheduleTaskExecutor);
  }
}
