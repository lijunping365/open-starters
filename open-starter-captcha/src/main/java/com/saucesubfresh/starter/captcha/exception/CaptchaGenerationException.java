package com.saucesubfresh.starter.captcha.exception;

/**
 * @author lijunping on 2022/11/2
 */
public class CaptchaGenerationException extends ValidateCodeException{

    public CaptchaGenerationException(String msg, Throwable t) {
        super(msg, t);
    }

    public CaptchaGenerationException(String msg) {
        super(msg);
    }
}
