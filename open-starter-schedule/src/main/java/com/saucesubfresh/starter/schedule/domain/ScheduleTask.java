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
package com.saucesubfresh.starter.schedule.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping
 */
@Data
public class ScheduleTask implements Serializable {
    private static final long serialVersionUID = -1936628567018899417L;
    /**
     * 任务 id
     */
    private Long taskId;

    /**
     * cron 表达式
     */
    private String cronExpression;
}
