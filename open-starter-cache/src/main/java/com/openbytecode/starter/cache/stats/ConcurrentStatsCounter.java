/*
 * Copyright 2014 Ben Manes. All Rights Reserved.
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
package com.openbytecode.starter.cache.stats;

import java.util.concurrent.atomic.LongAdder;

/**
 * This design is learning from {@link com.github.benmanes.caffeine.cache.stats.ConcurrentStatsCounter} which is in Caffeine.
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
