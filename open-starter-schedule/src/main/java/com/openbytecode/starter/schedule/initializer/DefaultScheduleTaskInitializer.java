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
package com.openbytecode.starter.schedule.initializer;

import com.openbytecode.starter.schedule.OpenJobScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultScheduleTaskInitializer implements ScheduleTaskInitializer, InitializingBean, DisposableBean {

    private final List<OpenJobScheduler> openJobSchedulerList;

    public DefaultScheduleTaskInitializer(List<OpenJobScheduler> openJobSchedulerList) {
        this.openJobSchedulerList = openJobSchedulerList;
    }


    @Override
    public void initialize() {
        for (OpenJobScheduler openJobScheduler : openJobSchedulerList) {
            openJobScheduler.start();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initialize();
            log.info("Schedule task initialize succeed");
        }catch (Exception e){
            log.error("Schedule task initialize failed, {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        for (OpenJobScheduler openJobScheduler : openJobSchedulerList) {
            openJobScheduler.stop();
        }
    }
}
