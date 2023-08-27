/*
 * Copyright © 2022 organization SauceSubFresh
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
package com.saucesubfresh.starter.schedule.wheel;

import com.saucesubfresh.starter.schedule.cron.CronHelper;
import com.saucesubfresh.starter.schedule.domain.WheelEntity;
import com.saucesubfresh.starter.schedule.exception.ScheduleException;
import com.saucesubfresh.starter.schedule.properties.ScheduleProperties;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 哈希时间轮实现
 *
 * @author lijunping
 */
public class HashedTimeWheel implements TimeWheel {

    private final long tickDuration;
    private static final Map<Integer, List<WheelEntity>> timeWheel = new ConcurrentHashMap<>();

    public HashedTimeWheel(ScheduleProperties scheduleProperties){
        long tickDuration = scheduleProperties.getTickDuration();
        if (tickDuration <= 0){
            throw new ScheduleException("The TaskPoolName cannot be empty.");
        }
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
        long round = diff / tickDuration + 1;
        int tick = (int) (nextTime % tickDuration);

        List<WheelEntity> taskList = timeWheel.getOrDefault(tick, new ArrayList<>());
        WheelEntity wheelEntity = new WheelEntity(taskId, round, cron);
        taskList.add(wheelEntity);
        timeWheel.put(tick, taskList);
    }

    @Override
    public List<WheelEntity> take(int slot) {
        List<WheelEntity> entities = timeWheel.get(slot);

        if (CollectionUtils.isEmpty(entities)){
            return Collections.emptyList();
        }

        List<WheelEntity> tasks = entities.stream().filter(e -> e.getRound() == 1L).collect(Collectors.toList());
        entities.removeAll(tasks);
        timeWheel.put(slot, entities);
        return tasks;
    }
}
