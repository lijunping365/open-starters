package com.lightcode.starter.security.exception;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证异常类
 */
public class SecurityException extends RuntimeException{

    public SecurityException(String msg) {
        super(msg);
    }
}
