/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.schedule.cron;

import com.openbytecode.starter.schedule.exception.ScheduleException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * @author lijunping
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
