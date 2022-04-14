package com.saucesubfresh.starter.alarm.properties;

import lombok.*;

/**
 * @author lijunping on 2022/4/14
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DingtalkAlarmProperties extends AlarmProperties{


    /**
     * 密钥
     */
    private String secret;

    /**
     * 自定义群机器人中的 webhook
     */
    private String webhook;

}
