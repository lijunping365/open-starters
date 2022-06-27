package com.saucesubfresh.starter.cache.exception;

import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2022-06-26 10:45
 */
@Data
public class CacheException extends RuntimeException{

    private final String cacheName;

    public CacheException(String cacheName, String message) {
        super(message);
        this.cacheName = cacheName;
    }
}
