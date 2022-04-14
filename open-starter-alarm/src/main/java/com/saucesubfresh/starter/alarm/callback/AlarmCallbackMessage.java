package com.saucesubfresh.starter.alarm.callback;

import com.saucesubfresh.starter.alarm.request.BaseAlarmMessage;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Alarm message represents the details of each alarm.
 */
@Data
public class AlarmCallbackMessage {
    /**
     * 报警内容
     */
    private BaseAlarmMessage alarmMessage;

    /**
     * 报警失败 key：发送失败的用户，value：失败原因
     */
    private Map<String,String> failedSender;

    /**
     * 发送时间
     */
    private LocalDateTime time;
}
