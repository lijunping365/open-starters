package com.saucesubfresh.starter.cache.message;

/**
 * @author lijunping on 2022/6/23
 */
public enum CacheSyncStrategy {

    /**
     * No synchronizations on map changes.
     */
    NONE,

    /**
     * Invalidate local cache entry across all LocalCachedMap instances on map entry change. Broadcasts map entry hash (16 bytes) to all instances.
     */
    INVALIDATE,

    /**
     * Update local cache entry across all LocalCachedMap instances on map entry change. Broadcasts full map entry state (Key and Value objects) to all instances.
     */
    UPDATE
}
