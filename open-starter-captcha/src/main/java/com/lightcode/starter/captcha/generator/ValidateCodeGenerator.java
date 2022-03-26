package com.lightcode.starter.captcha.generator;


import com.lightcode.starter.captcha.core.sms.ValidateCode;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description : 以后还会有别的验证码生成逻辑，故将其统一定义出来
 */
public interface ValidateCodeGenerator<C extends ValidateCode>{

    /**
     * 生成验证码
     */
    C generate() throws Exception;
}
