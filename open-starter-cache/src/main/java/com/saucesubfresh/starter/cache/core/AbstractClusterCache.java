package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.NullValue;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import com.saucesubfresh.starter.cache.stats.StatsCounter;

/**
 * @author lijunping on 2022/6/9
 */
public abstract class AbstractClusterCache implements ClusterCache {

    private final StatsCounter statsCounter;

    public AbstractClusterCache(StatsCounter statsCounter) {
        this.statsCounter = statsCounter;
    }

    @Override
    public CacheStats getStats() {
        return statsCounter.snapshot();
    }

    protected Object toValueWrapper(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().getName().equals(NullValue.class.getName())) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    protected Object toStoreValue(Object userValue) {
        if (userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

    protected void afterRead(Object value){
        if (value == null) {
            statsCounter.recordMisses(1);
        } else {
            statsCounter.recordHits(1);
        }
    }

    protected void afterPut(){
        statsCounter.recordHits(1);
    }
}
