package com.saucesubfresh.starter.cache.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/24
 */
@Data
public class CacheMessage implements Serializable {
    /**
     * The cache name
     */
    private String cacheName;
    /**
     * The cache key
     */
    private Object key;
    /**
     * The cache value
     */
    private Object value;
    /**
     * The cache instance id
     */
    private Long instanceId;
    /**
     * The message id
     */
    private String msgId;
    /**
     * The message command
     */
    private CacheCommand command;
}
