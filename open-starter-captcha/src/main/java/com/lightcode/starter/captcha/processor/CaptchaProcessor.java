package com.lightcode.starter.captcha.processor;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import com.lightcode.starter.captcha.request.CaptchaGenerateRequest;
import com.lightcode.starter.captcha.request.CaptchaVerifyRequest;
import com.lightcode.starter.captcha.send.ValidateCodeSend;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:校验码处理器，封装不同校验码的处理逻辑
 */
public interface CaptchaProcessor<C extends ValidateCode> {

    /**
     * 创建校验码
     */
    void create(CaptchaGenerateRequest request, ValidateCodeSend<C> validateCodeSend) throws Exception;

    /**
     * 校验验证码
     */
    void validate(CaptchaVerifyRequest request);
}
