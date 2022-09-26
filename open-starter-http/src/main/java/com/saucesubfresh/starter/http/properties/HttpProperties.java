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
package com.saucesubfresh.starter.http.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.http")
public class HttpProperties {

    /**
     * 连接池数量
     */
    private Integer poolSize = 200;

    /**
     * 某一个服务每次能并行接收的请求数量。
     */
    private Integer maxPerRoute = 500;

    /**
     * 连接超时时间，单位毫秒
     */
    private Integer timeOut = 5000;

    /**
     * 存活时长，单位 min
     */
    private long keepAlive = 5;
}
