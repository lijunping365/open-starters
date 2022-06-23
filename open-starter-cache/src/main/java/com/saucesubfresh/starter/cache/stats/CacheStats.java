package com.saucesubfresh.starter.cache.stats;

import lombok.Data;

/**
 * copy caffeine
 * @author lijunping on 2022/6/23
 */
@Data
public final class CacheStats {

    private final long hitCount;
    private final long missCount;
    private final long putsCount;

    public CacheStats(long hitCount, long missCount,long putsCount) {
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.putsCount = putsCount;
    }

    public double hitRate() {
        long requestCount = requestCount();
        return (requestCount == 0) ? 1.0 : (double) hitCount / requestCount;
    }

    public double missRate() {
        long requestCount = requestCount();
        return (requestCount == 0) ? 0.0 : (double) missCount / requestCount;
    }

    public long requestCount() {
        return saturatedAdd(hitCount, missCount);
    }

    /**
     * Returns the sum of {@code a} and {@code b} unless it would overflow or underflow in which case
     * {@code Long.MAX_VALUE} or {@code Long.MIN_VALUE} is returned, respectively.
     */
    private static long saturatedAdd(long a, long b) {
        long naiveSum = a + b;
        if ((a ^ b) < 0 | (a ^ naiveSum) >= 0) {
            // If a and b have different signs or a has the same sign as the result then there was no
            // overflow, return.
            return naiveSum;
        }
        // we did over/under flow, if the sign is negative we should return MAX otherwise MIN
        return Long.MAX_VALUE + ((naiveSum >>> (Long.SIZE - 1)) ^ 1);
    }
}
