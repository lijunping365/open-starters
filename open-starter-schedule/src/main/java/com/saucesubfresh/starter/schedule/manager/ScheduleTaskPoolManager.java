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

import java.util.Collection;
import java.util.List;

/**
 * 任务池实现
 *
 * @author lijunping
 */
public interface ScheduleTaskPoolManager {

    /**
     * 批量添加任务
     * @param task
     */
    void addAll(List<ScheduleTask> task);

    /**
     * 获取任务池中存在的全部任务
     * @return 任务池中存在的全部任务
     */
    Collection<ScheduleTask> getAll();

    /**
     * 获取即将被调度的任务
     * @return 调度任务集合
     */
    ScheduleTask get(Long taskId);

    /**
     * 添加任务
     * @param task
     */
    void add(ScheduleTask task);

    /**
     * 移除任务
     * @param taskId
     */
    void remove(Long taskId);
}
