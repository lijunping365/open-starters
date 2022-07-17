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
     * 注意：调用 {@link CronExpression#getNextValidTimeAfter(Date)} 返回的时间毫秒位是 0
     *
     * 所以这里返回的下次执行时间去掉了毫秒位，只保留到秒
     *
     * @param cronExpression cron 表达式
     * @return 下次执行时间 精确到秒
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

        return nextDate.getTime() / 1000;
    }
}
