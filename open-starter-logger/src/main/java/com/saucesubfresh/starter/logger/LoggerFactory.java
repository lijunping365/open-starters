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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * logger factory
 *
 * @author lijunping
 */
public final class LoggerFactory {

    public static final char DOT = '.';
    public static final char DOLLAR = '$';

    private static final Map<String, Logger> loggerCache = new ConcurrentHashMap<>();

    public static Logger getLogger(Class<?> clazz){
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name){
        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        // check if the desired logger exists, if it does, return it
        Logger oldLogger = loggerCache.get(name);

        if (oldLogger != null) {
            return oldLogger;
        }

        int i = 0;
        Logger logger = null;

        // if the desired logger does not exist, them create all the loggers
        // in between as well (if they don't already exist)
        String loggerName;
        while (true) {
            int h = getSeparatorIndexOf(name, i);
            if (h == -1) {
                loggerName = name;
            } else {
                loggerName = name.substring(0, h);
            }
            // move i left of the last point
            i = h + 1;
            synchronized (logger) {
                LoggerAppender loggerAppender = new DefaultFileLoggerAppender();
                logger = new DefaultLogger(loggerAppender);
                loggerCache.put(loggerName, logger);
            }
            if (h == -1) {
                return logger;
            }
        }
    }


    /**
     * Get the position of the separator character, if any, starting at position
     * 'fromIndex'.
     *
     * @param name
     * @param fromIndex
     * @return
     */
    public static int getSeparatorIndexOf(String name, int fromIndex) {
        int dotIndex = name.indexOf(DOT, fromIndex);
        int dollarIndex = name.indexOf(DOLLAR, fromIndex);

        if (dotIndex == -1 && dollarIndex == -1)
            return -1;
        if (dotIndex == -1)
            return dollarIndex;
        if (dollarIndex == -1)
            return dotIndex;

        return dotIndex < dollarIndex ? dotIndex : dollarIndex;
    }
}
