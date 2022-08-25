package com.saucesubfresh.starter.limiter.config;

import com.saucesubfresh.starter.limiter.core.RateLimiter;
import com.saucesubfresh.starter.limiter.core.RedissonRateLimiter;
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
    @ConditionalOnBean(RedissonClient.class)
    public RateLimiter rateLimiter(RedissonClient client){
        return new RedissonRateLimiter(client);
    }



}
