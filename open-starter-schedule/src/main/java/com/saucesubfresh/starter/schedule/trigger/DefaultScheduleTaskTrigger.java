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
import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.service.ScheduleTaskService;
import com.saucesubfresh.starter.schedule.wheel.TimeWheel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

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
        List<Long> taskIds = timeWheel.take();
        if (CollectionUtils.isEmpty(taskIds)){
            return;
        }

        refreshNextTime(taskIds);
        try {
            executor.execute(taskIds);
        }catch (Exception e){
            log.error("Execute task error:{}", e.getMessage(), e);
        }
    }

    /**
     * 刷新下次执行时间
     *
     * @param taskIds
     */
    private void refreshNextTime(List<Long> taskIds) {
        for (Long taskId : taskIds) {
            ScheduleTask task = scheduleTaskService.get(taskId);
            if (Objects.isNull(task)){
                continue;
            }
            try {
                timeWheel.put(taskId, task.getCronExpression());
            }catch (Exception e){
                log.error("Refresh task error:{}", e.getMessage(), e);
            }
        }
    }
}
