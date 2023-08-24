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
package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.trigger.TaskTrigger;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultTaskJobScheduler implements TaskJobScheduler {

    private Thread scheduleThread;
    private final TaskTrigger taskTrigger;
    private volatile boolean scheduleThreadToStop = false;

    public DefaultTaskJobScheduler(TaskTrigger taskTrigger) {
        this.taskTrigger = taskTrigger;
    }


    @Override
    public void start() {
        scheduleThread = new Thread(()->{
            while (!scheduleThreadToStop) {
                taskTrigger.trigger();
                threadSleep();
            }
            log.info("scheduleThread stop");
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("scheduleThread");
        scheduleThread.start();
        log.info("scheduleThread start success");
    }

    @Override
    public void stop() {
        scheduleThreadToStop = true;
        stopThread(scheduleThread);
        log.info("scheduleThread stop success");
    }


    protected void threadSleep(){
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            if (!scheduleThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 停止线程
     * @param thread 要停止的线程
     */
    private void stopThread(Thread thread){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        if (thread.getState() == Thread.State.TERMINATED){
            return;
        }

        // interrupt and wait
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
