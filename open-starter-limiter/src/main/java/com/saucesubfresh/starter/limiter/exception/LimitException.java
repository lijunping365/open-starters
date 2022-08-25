package com.saucesubfresh.starter.limiter.exception;

/**
 * @author lijunping on 2022/8/25
 */
public class LimitException extends RuntimeException{

    public LimitException(String message) {
        super(message);
    }
}
