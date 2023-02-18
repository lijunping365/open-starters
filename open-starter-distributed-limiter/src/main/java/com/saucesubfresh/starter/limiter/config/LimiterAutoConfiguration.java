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
package com.saucesubfresh.starter.limiter.config;

import com.saucesubfresh.starter.limiter.aspect.LimiterAspect;
import com.saucesubfresh.starter.limiter.generator.KeyGenerator;
import com.saucesubfresh.starter.limiter.generator.SimpleKeyGenerator;
import com.saucesubfresh.starter.limiter.processor.RateLimiter;
import com.saucesubfresh.starter.limiter.processor.RedissonRateLimiter;
import com.saucesubfresh.starter.limiter.properties.LimiterProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping
 */
@Configuration
@EnableConfigurationProperties(LimiterProperties.class)
public class LimiterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator limitKeyGenerator(){
        return new SimpleKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public RateLimiter rateLimiter(RedissonClient client){
        return new RedissonRateLimiter(client);
    }

    @Bean
    public LimiterAspect limiterAspect(RateLimiter rateLimiter, KeyGenerator keyGenerator){
        return new LimiterAspect(rateLimiter, keyGenerator);
    }

}
