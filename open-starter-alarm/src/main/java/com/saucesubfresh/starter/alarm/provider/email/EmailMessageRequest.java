package com.saucesubfresh.starter.alarm.provider.email;

import com.saucesubfresh.starter.alarm.request.BaseAlarmMessage;
import lombok.Data;

/**
 * @author lijunping on 2022/4/14
 */
@Data
public class EmailMessageRequest extends BaseAlarmMessage {

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 标题
     */
    private String title;

    /**
     * 报警内容
     */
    private String content;

}
