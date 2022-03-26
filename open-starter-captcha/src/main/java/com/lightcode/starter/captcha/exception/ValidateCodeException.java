package com.lightcode.starter.captcha.exception;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:继承spring-security异常类的基类
 */
public class ValidateCodeException extends RuntimeException {
    private static final long serialVersionUID = -75098325592950112L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
