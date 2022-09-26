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
package com.saucesubfresh.starter.cache.metrics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 缓存指标数据上报信息类
 *
 * @author lijunping on 2022/6/24
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CacheMetrics implements Serializable {
    private static final long serialVersionUID = 7323631218288394440L;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 总请求总数
     */
    private long requestCount;

    /**
     * 总命中总数
     */
    private long hitCount;

    /**
     * 总未命中总数
     */
    private long missCount;

    /**
     * 放入总数
     */
    private long putCount;

    /**
     * 命中率
     */
    private double hitRate;

    /**
     * 未命中率
     */
    private double missRate;
}
