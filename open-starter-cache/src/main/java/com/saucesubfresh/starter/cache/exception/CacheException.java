package com.saucesubfresh.starter.cache.exception;

import com.saucesubfresh.starter.cache.message.CacheMessage;
import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2022-06-26 10:45
 */
@Data
public class CacheException extends RuntimeException{

    private final CacheMessage cacheMessage;

    public CacheException(String errMsg, CacheMessage cacheMessage) {
        super(errMsg);
        this.cacheMessage = cacheMessage;
    }
}
