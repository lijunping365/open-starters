package com.saucesubfresh.starter.captcha.processor;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.request.CaptchaVerifyRequest;

/**
 * @author: 李俊平
 * @Date: 2022-05-08 15:19
 */
public interface CaptchaVerifyProcessor {
    /**
     * 校验验证码
     */
    void validate(CaptchaVerifyRequest request) throws ValidateCodeException;
}
