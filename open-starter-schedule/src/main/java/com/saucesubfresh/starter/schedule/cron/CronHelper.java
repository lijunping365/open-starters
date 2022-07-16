package com.saucesubfresh.starter.schedule.cron;

import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * @author: 李俊平
 * @Date: 2022-07-16 11:29
 */
@Slf4j
public class CronHelper {

    /**
     * 根据 cronExpression 生成下次执行时间
     *
     * @param cronExpression cron 表达式
     * @return 下次执行时间
     * @throws ScheduleException
     */
    public static long getNextTime(String cronExpression) throws ScheduleException{
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
}
