package com.lightcode.starter.security.exception;

import lombok.Data;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证异常类
 */
@Data
public class SecurityException extends RuntimeException{

    public SecurityException(String msg) {
        super(msg);
    }
}
