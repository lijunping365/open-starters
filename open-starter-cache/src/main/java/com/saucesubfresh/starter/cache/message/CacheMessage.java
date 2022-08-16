package com.saucesubfresh.starter.cache.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheMessage implements Serializable {
    private static final long serialVersionUID = 6665887883761953554L;
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
