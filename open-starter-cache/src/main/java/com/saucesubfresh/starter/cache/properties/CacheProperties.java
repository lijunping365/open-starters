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
package com.saucesubfresh.starter.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cache")
public class CacheProperties {
    /**
     * 键值条目的存活时间，以秒为单位，默认 30 min。
     */
    private long ttl = 1800;
    /**
     * 缓存容量（缓存数量最大值），默认 100 条
     */
    private int maxSize = 100;
    /**
     * 自动上报周期，默认 60，单位秒
     */
    private long metricsReportCycle = 60;
    /**
     * 是否存储空值，默认为 true
     */
    private boolean allowNullValues = true;
    /**
     * 是否开启自动缓存指标信息上报
     */
    private boolean enableMetricsReport = false;
    /**
     * 命名空间，相当于应用名称，应当改为自己的
     */
    private String namespace = "open-cache";
    /**
     * 缓存实例 id
     */
    private String instanceId = "127.0.0.1:8080";
    /**
     * 配置文件
     */
    private String configLocation = "classpath:/open-cache-config.yaml";
}
