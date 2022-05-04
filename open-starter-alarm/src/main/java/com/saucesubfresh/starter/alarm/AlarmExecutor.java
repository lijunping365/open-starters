package com.saucesubfresh.starter.alarm;

import com.saucesubfresh.starter.alarm.exception.AlarmException;
import com.saucesubfresh.starter.alarm.request.BaseAlarmMessage;

/**
 * @author lijunping on 2022/4/14
 */
public interface AlarmExecutor<T extends BaseAlarmMessage> {

    void doAlarm(T message) throws AlarmException;
}
