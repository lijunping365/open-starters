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
package com.openbytecode.starter.schedule.wheel;

import com.openbytecode.starter.schedule.properties.ScheduleProperties;
import com.openbytecode.starter.schedule.cron.CronHelper;
import com.openbytecode.starter.schedule.exception.ScheduleException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 哈希时间轮实现
 *
 * @author lijunping
 */
@Slf4j
public class HashedTimeWheel implements TimeWheel {

    private final long threshold;
    private final long tickDuration;
    private static final Map<Integer, Set<Long>> timeWheel = new ConcurrentHashMap<>();

    public HashedTimeWheel(ScheduleProperties scheduleProperties){
        long threshold = scheduleProperties.getThreshold();
        long tickDuration = scheduleProperties.getTickDuration();
        if (tickDuration <= 0){
            throw new ScheduleException("The tickDuration must greater than zero");
        }
        if (threshold <= 0 || threshold >= tickDuration){
            throw new ScheduleException("The threshold must greater than zero and less than tickDuration");
        }
        this.threshold = threshold;
        this.tickDuration = tickDuration;
    }

    @Override
    public void put(Long taskId, String cron) {
        long nextTime = CronHelper.getNextTime(cron);
        long nowTime = System.currentTimeMillis() / 1000;
        if (nextTime <= nowTime){
            throw new ScheduleException("The nextTime must more than the nowTime");
        }

        long diff = nextTime - nowTime;
        long round = diff / tickDuration;
        int tick = (int) (nextTime % tickDuration);

        if (round > 0L || diff > threshold){
            return;
        }

        Set<Long> taskList = timeWheel.getOrDefault(tick, new HashSet<>());
        taskList.add(taskId);
        timeWheel.put(tick, taskList);

        log.debug("HashedTimeWheel.timeWheel {}", timeWheel);
    }

    @Override
    public Set<Long> take(int slot) {
        return timeWheel.remove(slot);
    }
}