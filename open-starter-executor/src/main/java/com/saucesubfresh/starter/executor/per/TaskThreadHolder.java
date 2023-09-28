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
package com.saucesubfresh.starter.executor.per;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class TaskThreadHolder {

    private static final ConcurrentMap<Long, TaskThread> taskThreadMap = new ConcurrentHashMap<>();

    /**
     * 根据 taskId 获取绑定的线程
     * @param taskId
     * @return
     */
    public static TaskThread getThread(Long taskId) {
        return taskThreadMap.get(taskId);
    }

    /**
     * 将 taskId 和线程放入 map
     * @param taskId
     * @return
     */
    public static void putThread(Long taskId, TaskThread newTaskThread) {
        TaskThread oldTaskThread = taskThreadMap.put(taskId, newTaskThread);
        if (oldTaskThread != null) {
            oldTaskThread.toStop();
            oldTaskThread.interrupt();
        }
    }

    /**
     * 移除 taskId 绑定的线程
     * @param taskId
     */
    public static TaskThread removeThread(Long taskId) {
        TaskThread oldJobThread = taskThreadMap.remove(taskId);
        if (oldJobThread != null) {
            oldJobThread.toStop();
            oldJobThread.interrupt();
        }
        return oldJobThread;
    }

    /**
     * 释放资源
     */
    public static void shutdown(){
        if (taskThreadMap.size() == 0) {
            return;
        }

        for (Map.Entry<Long, TaskThread> item: taskThreadMap.entrySet()) {
            TaskThread oldTaskThread = removeThread(item.getKey());
            // wait for job thread write response
            if (oldTaskThread == null) {
                continue;
            }
            try {
                oldTaskThread.join();
            } catch (InterruptedException e) {
                log.error("TaskThread destroy(join) error, taskId:{}", item.getKey(), e);
            }
        }

        taskThreadMap.clear();
    }
}
