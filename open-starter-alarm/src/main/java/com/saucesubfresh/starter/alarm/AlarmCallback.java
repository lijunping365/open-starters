package com.saucesubfresh.starter.alarm;

import java.util.List;

/**
 * Alarm call back will be called by alarm implementor, after it decided alarm should be sent.
 */
public interface AlarmCallback {
    void doAlarm(List<AlarmMessage> alarmMessage);
}
