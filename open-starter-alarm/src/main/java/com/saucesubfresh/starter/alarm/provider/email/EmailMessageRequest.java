package com.saucesubfresh.starter.alarm.provider.email;

import com.saucesubfresh.starter.alarm.request.BaseAlarmMessage;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lijunping on 2022/4/14
 */
@Data
public class EmailMessageRequest extends BaseAlarmMessage {

    /**
     * 发件人
     */
    @NotBlank(message = "发件人不能为空")
    private String fromEmail;

    /**
     * 收件人，逗号隔开字符串
     */
    @NotBlank(message = "收件人不能为空")
    private String toEmail;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 报警内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

}
