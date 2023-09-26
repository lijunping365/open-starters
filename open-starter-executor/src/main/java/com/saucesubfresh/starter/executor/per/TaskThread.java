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

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskThread extends Thread{

    /* What will be run. */
    private Runnable target;

    private final Long taskId;

    private int idleTimes = 0;

    private volatile boolean stop = false;

    private final LinkedBlockingQueue<Task> queue;

    public TaskThread(Long taskId, Runnable runnable) {
        this.taskId = taskId;
        this.target = runnable;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (!stop){
            idleTimes++;
            Task task = null;

            try {
                task = queue.poll(3L, TimeUnit.SECONDS);
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

            if (Objects.nonNull(task)){
                idleTimes = 0;
                execute(task);
                continue;
            }

            if (idleTimes > 30 && queue.size() == 0) {
                TaskThreadHolder.removeThread(taskId);
            }
        }

        // handle job in queue when jobThread is killed
        while(queue.size() > 0){
            Task task = queue.poll();
            if (task != null) {
                // todo
            }
        }
    }

    public void offer(Task task) {
        queue.add(task);
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
