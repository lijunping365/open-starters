package com.saucesubfresh.starter.cache.stats;

import java.util.concurrent.atomic.LongAdder;

/**
 * copy caffeine
 * @author lijunping on 2022/6/23
 */
public final class ConcurrentStatsCounter implements StatsCounter{
    private final LongAdder hitCount;
    private final LongAdder missCount;
    private final LongAdder putsCount;

    public ConcurrentStatsCounter() {
        this.hitCount = new LongAdder();
        this.missCount = new LongAdder();
        this.putsCount = new LongAdder();
    }

    @Override
    public void recordHits(int count) {
        hitCount.add(count);
    }

    @Override
    public void recordMisses(int count) {
        missCount.add(count);
    }

    @Override
    public void recordPuts(int count) {
        putsCount.add(count);
    }

    @Override
    public CacheStats snapshot() {
        return new CacheStats(hitCount.sum(), missCount.sum(), putsCount.sum());
    }
}
