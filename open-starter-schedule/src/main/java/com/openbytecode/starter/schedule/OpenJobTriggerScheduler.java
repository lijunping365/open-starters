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
package com.openbytecode.starter.schedule;

import com.openbytecode.starter.schedule.executor.ScheduleTaskExecutor;
import com.openbytecode.starter.schedule.wheel.TimeWheel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Slf4j
public class OpenJobTriggerScheduler extends AbstractOpenJobScheduler {

    private Thread scheduleThread;
    private final TimeWheel timeWheel;
    private final ScheduleTaskExecutor executor;
    private volatile boolean scheduleThreadToStop = false;

    public OpenJobTriggerScheduler(TimeWheel timeWheel, ScheduleTaskExecutor executor) {
        this.timeWheel = timeWheel;
        this.executor = executor;
    }

    @Override
    public void start() {
        scheduleThread = new Thread(()->{
            while (!scheduleThreadToStop) {
                this.threadSleep();
                this.trigger();
            }
            log.info("triggerScheduleThread stop");
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("triggerScheduleThread");
        scheduleThread.start();
        log.info("triggerScheduleThread start success");
    }

    @Override
    public void stop() {
        scheduleThreadToStop = true;
        stopThread(scheduleThread);
        log.info("triggerScheduleThread stop success");
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

    public void trigger() {
        int slot = Calendar.getInstance().get(Calendar.SECOND);
        Set<Long> taskList = timeWheel.take(slot);
        if (CollectionUtils.isEmpty(taskList)){
            return;
        }

        try {
            executor.execute(new ArrayList<>(taskList));
        }catch (Exception e){
            log.error("Execute task error:{}", e.getMessage(), e);
        }
    }

}
