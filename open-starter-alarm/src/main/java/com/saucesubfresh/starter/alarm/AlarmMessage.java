package com.saucesubfresh.starter.alarm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Alarm message represents the details of each alarm.
 */
@Setter
@Getter
public class AlarmMessage {
    private int scopeId;
    private String scope;
    private String name;
    private String id0;
    private String id1;
    private String ruleName;
    private String alarmMessage;
    private List<String> tags;
    private long startTime;
    private transient int period;
    private transient boolean onlyAsCondition;
}
