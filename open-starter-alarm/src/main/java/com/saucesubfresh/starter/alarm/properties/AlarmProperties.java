package com.saucesubfresh.starter.alarm.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/4/14
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.alarm")
public class AlarmProperties {

    DingtalkAlarmProperties dingtalk = new DingtalkAlarmProperties();
}
