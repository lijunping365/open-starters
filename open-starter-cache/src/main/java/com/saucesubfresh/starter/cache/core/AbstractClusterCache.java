package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.SimpleValueWrapper;
import com.saucesubfresh.starter.cache.domain.ValueWrapper;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lijunping on 2022/6/9
 */
public abstract class AbstractClusterCache implements ClusterCache {

    private final AtomicLong hits = new AtomicLong();

    private final AtomicLong puts = new AtomicLong();

    private final AtomicLong misses = new AtomicLong();

    /**
     * 在取出缓存时调用
     */
    protected Object getWrapperValue(ValueWrapper valueWrapper) {
        return valueWrapper.get();
    }

    /**
     * 在进行缓存时调用，如果缓存值为 null对象则进行包装
     */
    protected ValueWrapper toValueWrapper(Object value) {
        return new SimpleValueWrapper(value);
    }

    protected long getCacheHits(){
        return hits.get();
    }

    protected long getCacheMisses(){
        return misses.get();
    }

    protected long getCachePuts() {
        return puts.get();
    }

    protected void addCachePut() {
        puts.incrementAndGet();
    }

    protected void addCacheHit(){
        hits.incrementAndGet();
    }

    protected void addCacheMiss(){
        misses.incrementAndGet();
    }

}
