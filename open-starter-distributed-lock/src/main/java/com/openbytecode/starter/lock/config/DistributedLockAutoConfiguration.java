/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.lock.config;

import com.openbytecode.starter.lock.generator.KeyGenerator;
import com.openbytecode.starter.lock.generator.SimpleKeyGenerator;
import com.openbytecode.starter.lock.processor.RedissonDistributedLockProcessor;
import com.openbytecode.starter.lock.aspect.DistributedLockAspect;
import com.openbytecode.starter.lock.processor.DistributedLockProcessor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping
 */
@Configuration
public class DistributedLockAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public KeyGenerator lockKeyGenerator(){
    return new SimpleKeyGenerator();
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(RedissonClient.class)
  public DistributedLockProcessor distributedLockProcessor(RedissonClient redissonClient) {
    return new RedissonDistributedLockProcessor(redissonClient);
  }

  @Bean
  public DistributedLockAspect distributedLockAspect(DistributedLockProcessor lockProcessor, KeyGenerator keyGenerator){
    return new DistributedLockAspect(lockProcessor, keyGenerator);
  }

}
