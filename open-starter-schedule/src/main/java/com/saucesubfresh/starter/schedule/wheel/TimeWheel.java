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

import com.saucesubfresh.starter.schedule.domain.WheelEntity;

import java.util.List;

/**
 * @author lijunping
 */
public interface TimeWheel {

    /**
     * 任务放入到时间轮
     *
     * @param taskId
     * @param cron
     */
    void put(Long taskId, String cron);

    /**
     * 获取时间轮指定槽位的任务
     *
     * @return
     */
    List<WheelEntity> take(int slot);
}