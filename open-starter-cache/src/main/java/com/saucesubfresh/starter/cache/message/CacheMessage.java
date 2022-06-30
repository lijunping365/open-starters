package com.saucesubfresh.starter.cache.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/24
 */
@Data
public class CacheMessage implements Serializable {

    private String cacheName;

    private String msgId;

    private Object key;

    private Object value;

    private CacheCommand command;
}
