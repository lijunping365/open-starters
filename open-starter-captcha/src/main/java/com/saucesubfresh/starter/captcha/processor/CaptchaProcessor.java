package com.saucesubfresh.starter.captcha.processor;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;
import com.saucesubfresh.starter.captcha.request.CaptchaGenerateRequest;
import com.saucesubfresh.starter.captcha.request.CaptchaVerifyRequest;
import com.saucesubfresh.starter.captcha.send.ValidateCodeSend;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:校验码处理器，封装不同校验码的处理逻辑
 */
public interface CaptchaProcessor {

    /**
     * 创建校验码
     */
    <C extends ValidateCode> void create(CaptchaGenerateRequest request, ValidateCodeSend<C> validateCodeSend) throws Exception;

    /**
     * 校验验证码
     */
    void validate(CaptchaVerifyRequest request);
}
