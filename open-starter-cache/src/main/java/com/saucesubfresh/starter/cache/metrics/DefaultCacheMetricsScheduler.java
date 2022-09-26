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

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import com.saucesubfresh.starter.cache.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 12:08
 */
@Slf4j
public class DefaultCacheMetricsScheduler implements CacheMetricsScheduler, InitializingBean, DisposableBean {

    private final CacheMetricsPusher pusher;
    private final CacheMetricsCollector collector;
    private final CacheProperties properties;
    private final ScheduledExecutorService executorService;

    public DefaultCacheMetricsScheduler(CacheMetricsPusher pusher,
                                        CacheProperties properties,
                                        CacheMetricsCollector collector) {
        this.pusher = pusher;
        this.collector = collector;
        this.properties = properties;
        this.executorService = Executors.newSingleThreadScheduledExecutor(
                new NamedThreadFactory(
                        "open-cache-metrics-pusher-executor",
                        true));
    }

    @Override
    public void triggerPushMetrics() {
        final long metricsReportCycle = properties.getMetricsReportCycle();
        executorService.scheduleAtFixedRate(() -> {
            List<CacheMetrics> cacheMetrics = collector.collectCacheMetrics();
            if (CollectionUtils.isEmpty(cacheMetrics)){
                return;
            }
            try{
                pusher.pushCacheMetrics(cacheMetrics);
            } catch (Exception e){
                log.error(e.getMessage(), e);
            }
        },0, metricsReportCycle, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean enableMetricsReport = properties.isEnableMetricsReport();
        if (enableMetricsReport){
            triggerPushMetrics();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (executorService != null){
            executorService.shutdown();
        }
    }
}
