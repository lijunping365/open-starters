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
package com.saucesubfresh.starter.executor;

import com.saucesubfresh.starter.executor.per.TaskThread;
import com.saucesubfresh.starter.executor.per.TaskThreadHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认线程池任务执行器
 *
 * @author lijunping
 */
@Slf4j
public class ThreadPerTaskExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable runnable) {
        TaskThread taskThread = new TaskThread(runnable);
        TaskThreadHolder.putThread(taskId, taskThread);
        taskThread.start();
    }

    @Override
    public void close() {
        ConcurrentMap<Long, TaskThread> taskThreadMap = TaskThreadHolder.getTaskThreadMap();
        if (taskThreadMap.size() > 0) {
            for (Map.Entry<Long, TaskThread> item: taskThreadMap.entrySet()) {
                TaskThread oldJobThread = TaskThreadHolder.removeThread(item.getKey());
                // wait for thread do something
                if (oldJobThread != null) {
                    try {
                      oldJobThread.join();
                    } catch (InterruptedException e) {
                      log.error("JobThread destroy(join) error, jobId:{}", item.getKey(), e);
                    }
                }
            }
            TaskThreadHolder.clear();
        }
    }

}
