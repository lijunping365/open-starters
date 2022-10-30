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

/**
 * @author: 李俊平
 * @Date: 2022-06-12 17:08
 */
public interface JobHandlerRegister {

    /**
     * 根据 handlerName 获取 OpenJobHandler
     * @param handlerName 绑定的 handlerName
     * @return OpenJobHandler
     */
    OpenJobHandler getJobHandler(String handlerName);
}
