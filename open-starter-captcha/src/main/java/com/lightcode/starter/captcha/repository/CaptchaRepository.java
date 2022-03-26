package com.lightcode.starter.captcha.repository;

import com.lightcode.starter.captcha.core.sms.ValidateCode;

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
    void save(String requestId, ValidateCode code);
    /**
     * 获取验证码
     * @param requestId
     * @return
     */
    String get(String requestId);
}
