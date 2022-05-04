package com.saucesubfresh.starter.alarm.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/4/14
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.alarm")
public class AlarmProperties {

    /**
     * 钉钉机器人密钥
     */
    private String secret;

    /**
     * 自定义群机器人中的 webhook
     */
    private String webhook;
}
