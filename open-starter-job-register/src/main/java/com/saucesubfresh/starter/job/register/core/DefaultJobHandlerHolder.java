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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lijunping
 */
@Slf4j
public class DefaultJobHandlerHolder implements JobHandlerHolder{

    protected final ConcurrentMap<String, OpenJobHandler> handlerMap = new ConcurrentHashMap<>();

    protected final ConcurrentMap<String, JobHandler> annotationMap = new ConcurrentHashMap<>();

    @Override
    public void putJobHandler(String handlerName, OpenJobHandler jobHandler) {
        if (handlerMap.get(handlerName) != null) {
            log.warn("open-job jobHandler[" + handlerName + "] naming conflicts.");
        }
        handlerMap.put(handlerName, jobHandler);
    }

    @Override
    public OpenJobHandler getJobHandler(String handlerName) {
        return handlerMap.get(handlerName);
    }

    @Override
    public List<OpenJobHandler> getAllJobHandler() {
        return new ArrayList<>(handlerMap.values());
    }

    @Override
    public void putJobHandlerAnnotation(String handlerName, JobHandler annotation) {
        annotationMap.put(handlerName, annotation);
    }

    @Override
    public List<JobHandler> getAllJobHandlerAnnotation() {
        return new ArrayList<>(annotationMap.values());
    }
}
