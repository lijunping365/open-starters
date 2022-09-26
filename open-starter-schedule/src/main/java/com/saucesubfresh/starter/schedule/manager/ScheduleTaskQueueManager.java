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

import java.util.List;

/**
 * @author lijunping
 */
public interface ScheduleTaskQueueManager {

    /**
     * 任务放入到队列
     *
     * @param taskId
     * @param nextTime
     */
    void put(Long taskId, Long nextTime);

    /**
     * 从任务队列获取任务
     *
     * @return
     */
    List<Long> take();
}
