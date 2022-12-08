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
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Slf4j
public abstract class AbstractTaskJobScheduler implements TaskJobScheduler {

    private Thread scheduleThread;
    private volatile boolean scheduleThreadToStop = false;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;
    private final ScheduleTaskQueueManager scheduleTaskQueueManager;

    public AbstractTaskJobScheduler(ScheduleTaskPoolManager scheduleTaskPoolManager,
                                    ScheduleTaskQueueManager scheduleTaskQueueManager) {
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
        this.scheduleTaskQueueManager = scheduleTaskQueueManager;
    }

    @Override
    public void start() {
        scheduleThread = new Thread(()->{
            while (!scheduleThreadToStop) {
                List<Long> taskIds = scheduleTaskQueueManager.take();
                if (CollectionUtils.isEmpty(taskIds)){
                    threadSleep();
                    continue;
                }
                refreshNextTime(taskIds);
                try {
                    this.runTask(taskIds);
                }catch (Exception e){
                    if (!scheduleThreadToStop) {
                        log.error("Execute task error:{}", e.getMessage(), e);
                    }
                }
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

    /**
     * 刷新下次执行时间
     *
     * @param taskIds
     */
    protected void refreshNextTime(List<Long> taskIds) {
        for (Long taskId : taskIds) {
            ScheduleTask task = scheduleTaskPoolManager.get(taskId);
            if (Objects.isNull(task)){
                continue;
            }
            try {
                scheduleTaskQueueManager.put(taskId, task.getCronExpression());
            }catch (Exception e){
                if (!scheduleThreadToStop) {
                    log.error("Refresh task error:{}", e.getMessage(), e);
                }
            }
        }
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

    protected abstract void runTask(List<Long> taskIds);
}
