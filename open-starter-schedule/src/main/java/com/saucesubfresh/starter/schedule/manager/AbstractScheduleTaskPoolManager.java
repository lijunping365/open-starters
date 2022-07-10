package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.cron.CronExpression;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * @author: 李俊平
 * @Date: 2022-07-10 10:14
 */
@Slf4j
public abstract class AbstractScheduleTaskPoolManager implements ScheduleTaskPoolManager {

    protected long getNextTime(String cronExpression) throws ScheduleException {
        Date date = new Date();
        Date nextDate;
        try {
            nextDate = (new CronExpression(cronExpression)).getNextValidTimeAfter(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new ScheduleException("CronExpression exception:" + e.getMessage());
        }

        return nextDate.getTime();
    }

    protected void setNextTime(ScheduleTask scheduleTask){
        String cronExpression = scheduleTask.getCronExpression();
        long nextTime = this.getNextTime(cronExpression);
        scheduleTask.setNextTime(nextTime);
    }
}
