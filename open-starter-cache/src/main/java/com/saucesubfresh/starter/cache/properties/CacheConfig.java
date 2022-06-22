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

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 是否允存储许空值
     */
    private boolean allowNullValues;

    /**
     * 键值条目的存活时间，以毫秒为单位。
     */
    private long ttl;

    /**
     * 缓存容量
     */
    private int maxSize;

    /**
     * 键值输入的最大空闲时间(毫秒)。
     */
    private long maxIdleTime;

}
