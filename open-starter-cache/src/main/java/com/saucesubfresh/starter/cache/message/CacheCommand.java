/*
 * Copyright © 2022 organization SauceSubFresh
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
package com.saucesubfresh.starter.cache.message;

import lombok.Getter;

/**
 * @author lijunping on 2022/6/23
 */
@Getter
public enum CacheCommand {
    /**
     * Invalidate local cache entry across all LocalCachedMap instances on map entry change. Broadcasts map entry hash (16 bytes) to all instances.
     */
    INVALIDATE,

    /**
     * Update local cache entry across all LocalCachedMap instances on map entry change. Broadcasts full map entry state (Key and Value objects) to all instances.
     */
    UPDATE,

    /**
     * Clear local cache entry across all LocalCachedMap instances on map entry change.
     */
    CLEAR,

    /**
     * Preload local cache entry across all LocalCachedMap instances on map entry change. Broadcasts map entry hash (16 bytes) to all instances.
     */
    PRELOAD,

    ;
}
