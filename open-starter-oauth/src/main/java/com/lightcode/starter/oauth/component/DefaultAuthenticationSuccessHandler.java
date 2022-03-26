package com.lightcode.starter.oauth.component;

import com.lightcode.starter.oauth.authentication.Authentication;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认的认证成功处理器
 */
@Slf4j
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(Authentication authentication) {
        log.info("[登录成功]-[用户编号：{}]", authentication.getUserDetails().getId());
    }
}
