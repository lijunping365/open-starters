package com.saucesubfresh.starter.cache.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/24
 */
@Data
public class CacheMessage implements Serializable {

    private String msgId;

    private Object key;

    private Object value;

    private String topic;

    private String cacheName;

    private CacheMessageCommand command;
}
