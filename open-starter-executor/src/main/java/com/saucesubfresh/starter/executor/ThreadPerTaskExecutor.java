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

import com.saucesubfresh.starter.executor.per.Executor;
import com.saucesubfresh.starter.executor.per.TaskThread;
import com.saucesubfresh.starter.executor.per.TaskThreadHolder;
import com.saucesubfresh.starter.executor.per.ThreadQueueNode;
import com.saucesubfresh.starter.executor.properties.TaskExecutorProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认线程池任务执行器
 *
 * @author lijunping
 */
@Slf4j
public class ThreadPerTaskExecutor implements TaskExecutor {

    private final TaskExecutorProperties properties;

    public ThreadPerTaskExecutor(TaskExecutorProperties properties) {
        this.properties = properties;
    }

    @Override
    public void execute(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends ThreadQueueNode> void execute(T node, Executor<T> executor) {
        TaskThread<T> taskThread = TaskThreadHolder.getThread(node.getTaskId());
        if (taskThread == null) {
            taskThread = createTaskThread(node, executor);
        }
        taskThread.offer(node);
    }

    @Override
    public void shutdown() {
        TaskThreadHolder.shutdown();
    }

    private <T extends ThreadQueueNode> TaskThread<T> createTaskThread(T node, Executor<T> executor){
        Integer timeout = properties.getTimeout();
        Integer maxIdleTimes = properties.getMaxIdleTimes();
        TaskThread<T> taskThread = new TaskThread<>(node.getTaskId(), timeout, maxIdleTimes, executor);
        taskThread.start();
        TaskThreadHolder.putThread(node.getTaskId(), taskThread);
        return taskThread;
    }
}
