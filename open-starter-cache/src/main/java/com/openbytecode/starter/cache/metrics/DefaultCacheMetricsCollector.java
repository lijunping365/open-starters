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
package com.openbytecode.starter.cache.metrics;

import com.openbytecode.starter.cache.manager.CacheManager;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijunping
 */
public class DefaultCacheMetricsCollector implements CacheMetricsCollector{

    private final CacheManager cacheManager;
    private final CacheMetricsBuilder cacheMetricsBuilder;

    public DefaultCacheMetricsCollector(CacheManager cacheManager, CacheMetricsBuilder cacheMetricsBuilder) {
        this.cacheManager = cacheManager;
        this.cacheMetricsBuilder = cacheMetricsBuilder;
    }

    @Override
    public List<CacheMetrics> collectCacheMetrics() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }
        return cacheNames.stream().map(cacheMetricsBuilder::buildCacheMetrics).collect(Collectors.toList());
    }
}
