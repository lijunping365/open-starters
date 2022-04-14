package com.saucesubfresh.starter.alarm.callback;

import java.util.List;

/**
 * Alarm call back will be called by alarm implementor, after it decided alarm should be sent.
 */
@FunctionalInterface
public interface AlarmCallback {

    void callback(AlarmCallbackMessage callbackMessage);
}
