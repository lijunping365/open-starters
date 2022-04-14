package com.saucesubfresh.starter.alarm.config;

import com.saucesubfresh.starter.alarm.properties.AlarmProperties;
import com.saucesubfresh.starter.alarm.provider.dingtalk.DingtalkAlarmExecutor;
import com.saucesubfresh.starter.alarm.provider.email.EmailAlarmExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author lijunping on 2022/4/14
 */
@Configuration
@EnableConfigurationProperties(AlarmProperties.class)
public class AlarmAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JavaMailSender.class)
    public EmailAlarmExecutor mailAlarmExecutor(JavaMailSender mailSender){
        return new EmailAlarmExecutor(mailSender);
    }

    @Bean
    @ConditionalOnMissingBean
    public DingtalkAlarmExecutor dingtalkAlarmExecutor(AlarmProperties alarmProperties){
        return new DingtalkAlarmExecutor(alarmProperties);
    }
}
