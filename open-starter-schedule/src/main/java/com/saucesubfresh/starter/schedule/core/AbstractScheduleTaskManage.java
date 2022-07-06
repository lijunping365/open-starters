package com.saucesubfresh.starter.schedule.core;

import com.saucesubfresh.starter.schedule.cron.CronExpression;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author lijunping on 2022/1/20
 */
@Slf4j
public abstract class AbstractScheduleTaskManage implements ScheduleTaskManage{

    protected static final Long UNIT = 1000L;

    @Override
    public List<ScheduleTask> takeScheduleTask() {
        Calendar instance = Calendar.getInstance();
        long nowSecond = instance.getTimeInMillis() / UNIT;
        long minSecond = nowSecond - UNIT;
        return take(minSecond, nowSecond);
    }

    /**
     * 获取下次执行的时间
     *
     * @param cronExpression cron 表达式
     * @return 次执行的时间
     * @throws ScheduleException
     */
    protected long generateNextValidTime(String cronExpression) throws ScheduleException {
        Date date = new Date();
        Date nextDate;
        try {
            nextDate = new CronExpression(cronExpression).getNextValidTimeAfter(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new ScheduleException("CronExpression exception:" + e.getMessage());
        }
        return nextDate.getTime() / UNIT;
    }

    protected abstract List<ScheduleTask> take(long minSecond, long maxSecond);
}
