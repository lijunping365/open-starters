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

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cache")
public class CacheProperties {
    /**
     * 缓存实例 id
     */
    private String instanceId = "127.0.0.1:8080";
    /**
     * 配置文件
     */
    private String configLocation = "classpath:/open-cache-config.yaml";
    /**
     * 命名空间，相当于应用名称，应当改为自己的
     */
    private String namespace = "open-cache";
    /**
     * 是否开启自动缓存指标信息上报
     */
    private boolean enableMetricsReport = false;
    /**
     * 自动上报周期，默认 60，单位秒
     */
    private long metricsReportCycle = 60;
    /**
     * 键值输入的最大空闲时间(毫秒)。
     */
    private long maxIdleTime = 720000;
    /**
     * 缓存容量（缓存数量最大值）
     */
    private int maxSize = 100;
    /**
     * 键值条目的存活时间，以毫秒为单位。
     */
    private long ttl = 30000;

}
