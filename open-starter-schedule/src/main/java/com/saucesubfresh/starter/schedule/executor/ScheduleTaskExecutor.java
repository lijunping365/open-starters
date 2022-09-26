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
package com.saucesubfresh.starter.schedule.executor;

import java.util.List;

/**
 * <p>
 *  调度任务执行器
 *
 *  建议使用多线程执行防止任务发生阻塞
 * </p>
 *
 * @author lijunping
 */
public interface ScheduleTaskExecutor {

    void execute(List<Long> taskList);
}
