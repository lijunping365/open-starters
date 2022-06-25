package com.saucesubfresh.starter.cache.metrics;

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
public class DefaultCacheMetricsTrigger implements CacheMetricsTrigger, InitializingBean, DisposableBean {

    private final long metricsPeriod;
    private final CacheMetricsPusher pusher;
    private final CacheMetricsCollector collector;
    private ScheduledExecutorService executorService;

    public DefaultCacheMetricsTrigger(long metricsPeriod,
                                      CacheMetricsPusher pusher,
                                      CacheMetricsCollector collector) {
        this.pusher = pusher;
        this.collector = collector;
        this.metricsPeriod = metricsPeriod;
        this.executorService = Executors.newSingleThreadScheduledExecutor(
                new NamedThreadFactory("open-cache-metrics-pusher-executor", true));
    }

    @Override
    public void triggerPushMetrics() {
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
        },0, metricsPeriod, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        triggerPushMetrics();
    }

    @Override
    public void destroy() throws Exception {
        if (executorService != null){
            executorService.shutdown();
        }
    }
}
