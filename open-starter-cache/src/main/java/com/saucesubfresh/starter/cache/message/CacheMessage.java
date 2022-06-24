package com.saucesubfresh.starter.cache.message;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/24
 */
public class CacheMessage implements Serializable {

    private String msgId;

    private byte[] body;
}
