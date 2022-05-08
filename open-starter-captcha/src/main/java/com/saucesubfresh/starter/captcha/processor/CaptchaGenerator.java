package com.saucesubfresh.starter.captcha.processor;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.request.CaptchaGenerateRequest;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:校验码处理器，封装不同校验码的处理逻辑
 */
public interface CaptchaGenerator<T extends ValidateCode> {

    /**
     * 创建校验码
     */
    T create(CaptchaGenerateRequest request) throws ValidateCodeException;
}
