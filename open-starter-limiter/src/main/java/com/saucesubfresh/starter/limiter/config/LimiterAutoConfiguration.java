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
 * @author lijunping on 2022/8/25
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
