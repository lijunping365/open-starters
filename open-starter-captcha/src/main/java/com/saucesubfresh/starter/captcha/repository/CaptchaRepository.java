package com.saucesubfresh.starter.captcha.repository;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description：保存、获取、移除验证码的模版接口
 */
public interface CaptchaRepository {
    /**
     * 保存验证码
     * @param requestId
     * @param code
     */
    <C extends ValidateCode> void save(String requestId, C code);
    /**
     * 获取验证码
     * @param requestId
     * @return
     */
    String get(String requestId);
}
