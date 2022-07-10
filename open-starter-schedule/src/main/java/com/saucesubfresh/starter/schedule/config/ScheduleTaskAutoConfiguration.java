package com.saucesubfresh.starter.schedule.config;


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
import com.saucesubfresh.starter.schedule.scheduler.DefaultTaskConsumerScheduler;
import com.saucesubfresh.starter.schedule.scheduler.DefaultTaskProducerScheduler;
import com.saucesubfresh.starter.schedule.scheduler.TaskConsumerScheduler;
import com.saucesubfresh.starter.schedule.scheduler.TaskProducerScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
  public ScheduleTaskPoolManager scheduleTaskManager(){
    return new LocalScheduleTaskPoolManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskQueueManager scheduleTaskQueueManager(){
    return new HashedWheelScheduleTaskQueueManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskLoader scheduleTaskLoader(){
    return new DefaultScheduleTaskLoader();
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskInitializer taskInitializer(ScheduleTaskLoader scheduleTaskLoader,
                                                 ScheduleTaskPoolManager scheduleTaskPoolManager){
    return new DefaultScheduleTaskInitializer(scheduleTaskLoader, scheduleTaskPoolManager);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScheduleTaskExecutor scheduleTaskExecutor(){
    return new DefaultScheduleTaskExecutor();
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskProducerScheduler taskProducerScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager,
                                                     ScheduleTaskQueueManager scheduleTaskQueueManager){
    return new DefaultTaskProducerScheduler(scheduleTaskPoolManager, scheduleTaskQueueManager);
  }

  @Bean
  @ConditionalOnMissingBean
  public TaskConsumerScheduler taskConsumerScheduler(ScheduleTaskExecutor scheduleTaskExecutor,
                                                     ScheduleTaskQueueManager scheduleTaskQueueManager){
    return new DefaultTaskConsumerScheduler(scheduleTaskExecutor, scheduleTaskQueueManager);
  }

}
