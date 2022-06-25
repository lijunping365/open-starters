package com.saucesubfresh.starter.cache.metrics;

import com.saucesubfresh.starter.cache.thread.NamedThreadFactory;
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
public class DefaultCacheMetricsTrigger implements CacheMetricsTrigger, InitializingBean {

    private final long metricsPeriod;
    private final CacheMetricsPusher pusher;
    private final CacheMetricsCollector collector;

    public DefaultCacheMetricsTrigger(long metricsPeriod,
                                      CacheMetricsPusher pusher,
                                      CacheMetricsCollector collector) {
        this.pusher = pusher;
        this.collector = collector;
        this.metricsPeriod = metricsPeriod;
    }

    @Override
    public void triggerPushMetrics() {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor(
                        new NamedThreadFactory(
                                "open-cache-metrics-pusher-executor",
                                true));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<CacheMetrics> cacheMetrics = collector.collectCacheMetrics();
            if (CollectionUtils.isEmpty(cacheMetrics)){
                return;
            }
            pusher.pushCacheMetrics(cacheMetrics);
        },0, metricsPeriod, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        triggerPushMetrics();
    }
}
