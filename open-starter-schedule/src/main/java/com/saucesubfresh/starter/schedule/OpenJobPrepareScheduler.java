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

import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import com.saucesubfresh.starter.schedule.service.ScheduleTaskService;
import com.saucesubfresh.starter.schedule.wheel.TimeWheel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Slf4j
public class OpenJobPrepareScheduler extends AbstractOpenJobScheduler {

    private Thread scheduleThread;
    private final TimeWheel timeWheel;
    private final ScheduleTaskService scheduleTaskService;
    private volatile boolean scheduleThreadToStop = false;

    public OpenJobPrepareScheduler(TimeWheel timeWheel, ScheduleTaskService scheduleTaskService) {
        this.timeWheel = timeWheel;
        this.scheduleTaskService = scheduleTaskService;
    }


    @Override
    public void start() {
        scheduleThread = new Thread(()->{
            while (!scheduleThreadToStop) {
                long start = System.currentTimeMillis();
                this.prepare();
                long cost = System.currentTimeMillis()-start;
                if (cost < 1000) {
                    this.threadSleep();
                }
            }
            log.info("prepareScheduleThread stop");
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("prepareScheduleThread");
        scheduleThread.start();
        log.info("prepareScheduleThread start success");
    }

    @Override
    public void stop() {
        scheduleThreadToStop = true;
        stopThread(scheduleThread);
        log.info("prepareScheduleThread stop success");
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

    private void prepare(){
        Collection<ScheduleTask> scheduleTasks = scheduleTaskService.loadTask();
        if (CollectionUtils.isEmpty(scheduleTasks)){
            return;
        }

        for (ScheduleTask task : scheduleTasks) {
            timeWheel.put(task.getTaskId(), task.getCronExpression());
        }
    }
}
