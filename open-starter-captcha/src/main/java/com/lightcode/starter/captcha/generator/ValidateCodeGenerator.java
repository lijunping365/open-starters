package com.lightcode.starter.captcha.generator;


import com.lightcode.starter.captcha.core.sms.ValidateCode;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public interface ValidateCodeGenerator{

    /**
     * 生成验证码
     */
    ValidateCode generate() throws Exception;
}
