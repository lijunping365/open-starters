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
package com.saucesubfresh.starter.logger;

import com.saucesubfresh.starter.logger.properties.LoggerProperties;
import com.saucesubfresh.starter.logger.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * default logger evict
 *
 * @author lijunping
 */
@Slf4j
public class DefaultLoggerEvict implements LoggerEvict{

    private static final String FORMATTER = "yyyy-MM-dd";
    private Thread logEvictThread;
    private volatile boolean toStop = false;

    private final LoggerProperties properties;

    public DefaultLoggerEvict(LoggerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void start() {
        logEvictThread = new Thread(()->{
            while (!toStop) {
                evict();
                threadSleep();
            }
            log.info("logEvictThread stop");
        });
        logEvictThread.setDaemon(true);
        logEvictThread.setName("logEvictThread");
        logEvictThread.start();
        log.info("logEvictThread start success");
    }

    @Override
    public void stop() {
        toStop = true;

        if (logEvictThread == null) {
            return;
        }

        // interrupt and wait
        logEvictThread.interrupt();
        try {
            logEvictThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void evict() {
        Integer logRetentionDays = properties.getLogRetentionDays();
        if (logRetentionDays <= 0){
            return;
        }

        try {
            // clean log dir, over logRetentionDays
            File[] childDirs = new File(LoggerContext.getLogBasePath()).listFiles();
            if (childDirs == null || childDirs.length == 0) {
                return;
            }

            // today
            LocalDate todayDate = LocalDate.now();

            for (File childFile: childDirs) {

                // valid
                if (!childFile.isDirectory()) {
                    continue;
                }
                if (childFile.getName().indexOf("-") == -1) {
                    continue;
                }

                // file create date
                LocalDate logFileCreateDate = LocalDate.parse(childFile.getName(), DateTimeFormatter.ofPattern(FORMATTER));

                final int days = (int) ChronoUnit.DAYS.between(logFileCreateDate, todayDate);

                if (days >= properties.getLogRetentionDays()) {
                    FileUtil.deleteRecursively(childFile);
                }
            }
        } catch (Exception e) {
            if (!toStop) {
                log.error(e.getMessage(), e);
            }

        }
    }

    private void threadSleep() {
        try {
            TimeUnit.DAYS.sleep(1);
        } catch (InterruptedException e) {
            if (!toStop) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
