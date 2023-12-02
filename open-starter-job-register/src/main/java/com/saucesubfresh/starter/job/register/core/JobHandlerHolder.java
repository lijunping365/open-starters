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
package com.saucesubfresh.starter.job.register.core;

import com.saucesubfresh.starter.job.register.annotation.JobHandler;

import java.util.List;

/**
 * @author lijunping
 */
public interface JobHandlerHolder {

    /**
     * 设置 OpenJobHandler
     */
    void putJobHandler(String handlerName, OpenJobHandler jobHandler);

    /**
     * 根据 handlerName 获取 OpenJobHandler
     */
    OpenJobHandler getJobHandler(String handlerName);

    /**
     * 获取全部 OpenJobHandler
     */
    List<OpenJobHandler> getAllJobHandler();

    /**
     * 设置 JobHandler 注解
     */
    void putJobHandlerAnnotation(String handlerName, JobHandler annotation);

    /**
     * 获取全部 JobHandler 注解
     */
    List<JobHandler> getAllJobHandlerAnnotation();
}
