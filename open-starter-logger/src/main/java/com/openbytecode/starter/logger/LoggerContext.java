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
package com.openbytecode.starter.logger;

/**
 * logger context
 *
 * @author lijunping
 */
public class LoggerContext {

    private static final InheritableThreadLocal<LoggerContext> contextHolder = new InheritableThreadLocal<>();

    private final String logFileName;

    private static String logBasePath;

    public LoggerContext(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public static LoggerContext getLoggerContext(){
        return contextHolder.get();
    }

    public static void setLoggerContext(LoggerContext jobThreadContext){
        contextHolder.set(jobThreadContext);
    }

    public static String getLogBasePath() {
        return logBasePath;
    }

    public static void setLogBasePath(String logBasePath) {
        LoggerContext.logBasePath = logBasePath;
    }
}
