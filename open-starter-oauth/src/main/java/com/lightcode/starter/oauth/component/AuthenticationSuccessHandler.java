package com.lightcode.starter.oauth.component;

import com.lightcode.starter.oauth.authentication.Authentication;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证成功处理器
 */
public interface AuthenticationSuccessHandler {

    /**
     * 登录成功处理
     */
    void onAuthenticationSuccess(Authentication authentication);
}
