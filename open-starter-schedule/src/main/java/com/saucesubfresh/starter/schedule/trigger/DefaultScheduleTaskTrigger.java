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
package com.saucesubfresh.starter.schedule.trigger;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.domain.WheelEntity;
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.service.ScheduleTaskService;
import com.saucesubfresh.starter.schedule.wheel.TimeWheel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultScheduleTaskTrigger implements ScheduleTaskTrigger {

    private final TimeWheel timeWheel;
    private final ScheduleTaskService scheduleTaskService;
    private final ScheduleTaskExecutor executor;

    public DefaultScheduleTaskTrigger(TimeWheel timeWheel,
                                      ScheduleTaskService scheduleTaskService,
                                      ScheduleTaskExecutor executor) {
        this.timeWheel = timeWheel;
        this.scheduleTaskService = scheduleTaskService;
        this.executor = executor;
    }


    @Override
    public void trigger() {
        int slot = Calendar.getInstance().get(Calendar.SECOND);
        List<WheelEntity> taskList = timeWheel.take(slot);
        if (CollectionUtils.isEmpty(taskList)){
            return;
        }

        List<ScheduleTask> scheduleTasks = refreshNextTime(taskList);
        List<Long> taskIds = scheduleTasks.stream().map(ScheduleTask::getTaskId).collect(Collectors.toList());

        try {
            executor.execute(taskIds);
        }catch (Exception e){
            log.error("Execute task error:{}", e.getMessage(), e);
        }
    }

    /**
     * 刷新下次执行时间
     *
     * @param taskList
     * @return 任务列表
     */
    private List<ScheduleTask> refreshNextTime(List<WheelEntity> taskList) {
        List<ScheduleTask> tasks = new ArrayList<>();
        for (WheelEntity entity : taskList) {
            ScheduleTask task = scheduleTaskService.get(entity.getTaskId());
            if (Objects.isNull(task)){
                continue;
            }

            try {
                timeWheel.put(task.getTaskId(), task.getCronExpression());
            }catch (Exception e){
                log.error("Refresh task error:{}", e.getMessage(), e);
            }

            if (!StringUtils.equals(entity.getCron(), task.getCronExpression())){
                continue;
            }

            tasks.add(task);
        }
        return tasks;
    }
}
