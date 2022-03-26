package com.lightcode.starter.oauth.token;

import com.lightcode.starter.oauth.authentication.Authentication;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: token store
 */
public interface TokenStore {

    /**
     * 读取认证信息
     * 通过请求头中的 token 判断是不是我们系统颁发的，并判断token有没有过期
     * 如果根据 accessToken 未找到用户信息，或者 accessToken 已过期，就抛出 401
     * @param accessToken
     * @return
     */
    Authentication readAuthentication(String accessToken);

    /**
     * 生成 access_token 和 refresh_token
     * @param authentication
     * @return
     */
    AccessToken generateToken(Authentication authentication);

}
