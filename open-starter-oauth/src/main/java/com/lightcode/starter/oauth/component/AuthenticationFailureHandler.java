package com.lightcode.starter.oauth.component;


import com.lightcode.starter.oauth.exception.AuthenticationException;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证失败处理器
 */
public interface AuthenticationFailureHandler {

    /**
     * 登录失败处理
     */
    void onAuthenticationFailureHandler(AuthenticationException authenticationException);
}
