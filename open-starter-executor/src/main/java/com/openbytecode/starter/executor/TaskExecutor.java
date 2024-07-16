/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.executor;

import com.openbytecode.starter.executor.per.Executor;
import com.openbytecode.starter.executor.per.ThreadQueueNode;

/**
 * 任务的执行器接口
 *
 * @author lijunping
 */
public interface TaskExecutor {

    /**
     * The task execute by {@link ThreadPoolTaskExecutor}
     * @param command
     */
    void execute(Runnable command);

    /**
     * The task execute by {@link ThreadPerTaskExecutor}
     * @param node
     * @param executor
     */
    <T extends ThreadQueueNode> void execute(T node, Executor<T> executor);

    /**
     * This method will wait for previously submitted tasks to
     * complete execution. Use {@link ThreadPerTaskExecutor}
     * This method does not wait for previously submitted tasks to
     * complete execution. Use {@link ThreadPoolTaskExecutor}
     */
    void shutdown();

}
