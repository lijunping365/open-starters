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

/**
 * logger interface
 *
 * @author lijunping
 */
public interface Logger {

    /**
     * Log a message.
     *
     * @param e the throwable to be logged
     */
    void log(Throwable e);

    /**
     * Log a message.
     *
     * @param msg the message string to be logged
     */
    void log(String msg);

    /**
     * Log a message according to the specified format
     * and argument.
     *
     * @param format the format string
     * @param arg    the argument
     */
    void log(String format, Object arg);

    /**
     * Log a message according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param arg1   the first argument
     * @param arg2   the second argument
     */
    void log(String format, Object arg1, Object arg2);

    /**
     * Log a message according to the specified format
     * and arguments.
     * <p/>
     * {@link #log(String, Object) one} and {@link #log(String, Object, Object) two}
     * arguments exist solely in order to avoid this hidden cost.</p>
     *
     * @param format    the format string
     * @param arguments a list of 3 or more arguments
     */
    void log(String format, Object ... arguments);

}
