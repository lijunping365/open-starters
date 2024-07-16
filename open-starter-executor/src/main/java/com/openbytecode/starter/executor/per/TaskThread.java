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
package com.openbytecode.starter.executor.per;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskThread<T extends ThreadQueueNode> extends Thread{

    private final Long taskId;

    private int idleTimes = 0;

    private volatile boolean stop = false;

    private final Integer timeout;

    private final Integer maxIdleTimes;

    private final Executor<T> executor;

    private final LinkedBlockingQueue<T> workQueue;

    public TaskThread(Long taskId, Integer timeout, Integer maxIdleTimes, Executor<T> executor) {
        this.taskId = taskId;
        this.timeout = timeout;
        this.maxIdleTimes = maxIdleTimes;
        this.executor = executor;
        this.workQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (!stop){
            idleTimes++;
            T queueNode = null;

            try {
                queueNode = workQueue.poll(timeout, TimeUnit.SECONDS);
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

            if (Objects.nonNull(queueNode)){
                idleTimes = 0;
                executor.execute(queueNode, stop);
                continue;
            }

            if (idleTimes > maxIdleTimes && workQueue.size() == 0) {
                TaskThreadHolder.removeThread(taskId);
            }
        }

        // handle job in queue when taskThread is killed
        while(workQueue.size() > 0){
            T queueNode = workQueue.poll();
            if (queueNode != null) {
                executor.execute(queueNode, stop);
            }
        }
    }

    public void offer(T queueNode) {
        workQueue.add(queueNode);
    }

    /**
     * kill job thread
     *
     * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
     * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
     * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
     */
    public void toStop() {
        this.stop = true;
    }
}
