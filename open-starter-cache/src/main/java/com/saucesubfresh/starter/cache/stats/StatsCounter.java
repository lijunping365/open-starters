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
package com.saucesubfresh.starter.cache.stats;

/**
 * This design is learning from {@link com.github.benmanes.caffeine.cache.stats.StatsCounter} which is in Caffeine.
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
