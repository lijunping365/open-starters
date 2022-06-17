package com.saucesubfresh.starter.cache.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lijunping on 2022/6/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CacheConfig {

    private String cacheName;

    private int maxSize;

    private long ttl;

}
