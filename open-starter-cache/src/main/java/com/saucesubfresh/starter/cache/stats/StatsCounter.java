package com.saucesubfresh.starter.cache.stats;

/**
 * copy caffeine
 * @author lijunping on 2022/6/23
 */
public interface StatsCounter {

    /**
     * Records cache hits. This should be called when a cache request returns a cached value.
     *
     * @param count the number of hits to record
     */
    void recordHits(int count);

    /**
     * Records cache misses.
     *
     * @param count the number of misses to record
     */
    void recordMisses(int count);

    /**
     * Records cache puts.
     *
     * @param count the number of put to record
     */
    void recordPuts(int count);

    /**
     * Get the stats record
     *
     * @return stats record
     */
    CacheStats snapshot();
}
