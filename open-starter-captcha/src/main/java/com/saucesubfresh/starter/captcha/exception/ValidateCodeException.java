package com.saucesubfresh.starter.captcha.exception;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public class ValidateCodeException extends RuntimeException {
    private static final long serialVersionUID = -75098325592950112L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
