package com.saucesubfresh.starter.lock.config;

import com.saucesubfresh.starter.lock.aspect.DistributedLockAspect;
import com.saucesubfresh.starter.lock.processor.RedissonDistributedLockProcessor;
import com.saucesubfresh.starter.lock.processor.DistributedLockProcessor;
import com.saucesubfresh.starter.lock.generator.KeyGenerator;
import com.saucesubfresh.starter.lock.generator.SimpleKeyGenerator;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 李俊平
 * @Date: 2020-11-11 11:20
 */
@Configuration
public class DistributedLockAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(RedissonClient.class)
  DistributedLockProcessor distributedLockProcessor(RedissonClient redissonClient) {
    return new RedissonDistributedLockProcessor(redissonClient);
  }

  @Bean
  DistributedLockAspect distributedLockAspect(DistributedLockProcessor lockProcessor, KeyGenerator keyGenerator){
    return new DistributedLockAspect(lockProcessor, keyGenerator);
  }

  @Bean
  @ConditionalOnMissingBean
  KeyGenerator keyGenerator(){
    return new SimpleKeyGenerator();
  }

}
