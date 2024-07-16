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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * default logger
 *
 * @author lijunping
 */
public class DefaultLogger implements Logger{

    private static final String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void log(String msg) {
        log(msg, null);
    }

    @Override
    public void log(String format, Object arg) {
        log(format, new Object[] { arg });
    }

    @Override
    public void log(String format, Object arg1, Object arg2) {
        log(format, new Object[] { arg1, arg2 });
    }

    @Override
    public void log(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        String appendLog = ft.getMessage();

        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(callInfo, appendLog);
    }

    @Override
    public void log(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();

        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(callInfo, appendLog);
    }

    private void logDetail(StackTraceElement callInfo, String appendLog) {
        LoggerContext loggerContext = LoggerContext.getLoggerContext();
        if (loggerContext == null) {
            return;
        }

        String logFileName = loggerContext.getLogFileName();
        if (StringUtils.isBlank(logFileName)) {
            return;
        }

        /*// "yyyy-MM-dd HH:mm:ss [ClassName]-[MethodName]-[LineNumber]-[ThreadName] log";
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        StackTraceElement callInfo = stackTraceElements[1];*/

        StringBuffer stringBuffer = new StringBuffer();
        String timeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMATTER));
        stringBuffer.append(timeFormat).append(" ")
                .append("["+ callInfo.getClassName() + "#" + callInfo.getMethodName() +"]").append("-")
                .append("["+ callInfo.getLineNumber() +"]").append("-")
                .append("["+ Thread.currentThread().getName() +"]").append(" ")
                .append(appendLog != null ? appendLog : "");

        String formatAppendLog = stringBuffer.toString();
        LoggerAppender.appendLog(logFileName, formatAppendLog);
    }
}
