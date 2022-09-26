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
package com.saucesubfresh.starter.schedule.manager;

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 适用于单节点
 *
 * @author lijunping
 */
public class LocalScheduleTaskPoolManager implements ScheduleTaskPoolManager {

    private final ConcurrentMap<Long, ScheduleTask> taskMap = new ConcurrentHashMap<>(16);

    @Override
    public void addAll(List<ScheduleTask> taskList) {
        if (CollectionUtils.isEmpty(taskList)){
            return;
        }
        Map<Long, ScheduleTask> taskMap = taskList.stream().collect(Collectors.toMap(ScheduleTask::getTaskId, o -> o));
        this.taskMap.putAll(taskMap);
    }

    @Override
    public Collection<ScheduleTask> getAll() {
        return taskMap.values();
    }

    @Override
    public ScheduleTask get(Long taskId) {
        return taskMap.get(taskId);
    }

    @Override
    public void add(ScheduleTask scheduleTask) {
        taskMap.put(scheduleTask.getTaskId(), scheduleTask);
    }

    @Override
    public void remove(Long taskId) {
        taskMap.remove(taskId);
    }
}
