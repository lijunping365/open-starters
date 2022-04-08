package com.saucesubfresh.starter.oauth.component;

import com.saucesubfresh.starter.oauth.authentication.Authentication;

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
