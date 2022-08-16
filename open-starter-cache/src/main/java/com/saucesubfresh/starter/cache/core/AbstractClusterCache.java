package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.NullValue;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import com.saucesubfresh.starter.cache.stats.StatsCounter;

/**
 * @author lijunping on 2022/6/9
 */
public abstract class AbstractClusterCache implements ClusterCache {

    private static final String SAM = ":";
    private final StatsCounter statsCounter;

    public AbstractClusterCache(StatsCounter statsCounter) {
        this.statsCounter = statsCounter;
    }

    @Override
    public CacheStats getStats() {
        return statsCounter.snapshot();
    }

    /**
     * <p>
     *     toValueWrapper
     * </p>
     */
    protected Object toValueWrapper(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().getName().equals(NullValue.class.getName())) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    /**
     * <p>
     *     toStoreValue
     * </p>
     */
    protected Object toStoreValue(Object userValue) {
        if (userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

    /**
     * <p>
     *     在 Get 之后增加命中率
     * </p>
     */
    protected void afterRead(Object value){
        if (value == null) {
            statsCounter.recordMisses(1);
        } else {
            statsCounter.recordHits(1);
        }
    }

    /**
     * <p>
     *     再 Put 之后增加存放次数
     * </p>
     */
    protected void afterPut(){
        statsCounter.recordPuts(1);
    }

    /**
     * <p>
     *     这里解释下缓存名称为什么要拼接命名空间
     *
     *     为了避免多个应用使用同一个 redis 时 cacheName 相同
     * </p>
     *
     * @param namespace 命名空间
     * @param cacheName 缓存名称
     * @return 命名空间 + 缓存名称
     */
    protected String generate(String namespace, String cacheName){
        return namespace.concat(SAM).concat(cacheName);
    }
}
