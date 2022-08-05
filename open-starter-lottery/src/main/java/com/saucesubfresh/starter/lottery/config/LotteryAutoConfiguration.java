package com.saucesubfresh.starter.lottery.config;

import com.saucesubfresh.starter.lottery.component.DefaultLotteryExecuteFailureHandler;
import com.saucesubfresh.starter.lottery.component.DefaultLotteryExecuteSuccessHandler;
import com.saucesubfresh.starter.lottery.component.LotteryExecuteFailureHandler;
import com.saucesubfresh.starter.lottery.component.LotteryExecuteSuccessHandler;
import com.saucesubfresh.starter.lottery.executor.DefaultRedPacketLotteryExecutor;
import com.saucesubfresh.starter.lottery.executor.DefaultWheelLotteryExecutor;
import com.saucesubfresh.starter.lottery.executor.RedPacketLotteryExecutor;
import com.saucesubfresh.starter.lottery.executor.WheelLotteryExecutor;
import com.saucesubfresh.starter.lottery.filter.FilterFactory;
import com.saucesubfresh.starter.lottery.filter.LotteryFilterChain;
import com.saucesubfresh.starter.lottery.interceptor.DefaultLotteryAfterInterceptor;
import com.saucesubfresh.starter.lottery.interceptor.DefaultLotteryBeforeInterceptor;
import com.saucesubfresh.starter.lottery.interceptor.LotteryAfterInterceptor;
import com.saucesubfresh.starter.lottery.interceptor.LotteryBeforeInterceptor;
import com.saucesubfresh.starter.lottery.properties.LotteryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 李俊平
 * @Date: 2022-07-31 11:22
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LotteryProperties.class)
public class LotteryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FilterFactory filterFactory(){
        return new FilterFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public LotteryFilterChain filterChain(FilterFactory filterFactory){
        return new LotteryFilterChain(filterFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedPacketLotteryExecutor redPacketLotteryExecutor(){
        return new DefaultRedPacketLotteryExecutor();
    }

    @Bean
    @ConditionalOnMissingBean
    public WheelLotteryExecutor wheelLotteryExecutor(){
        return new DefaultWheelLotteryExecutor()
    }
}
