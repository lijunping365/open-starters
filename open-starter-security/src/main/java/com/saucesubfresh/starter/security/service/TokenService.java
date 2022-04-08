package com.saucesubfresh.starter.security.service;


import com.saucesubfresh.starter.security.domain.Authentication;

/**
 * @author lijunping on 2021/10/22
 */
public interface TokenService {
    /**
     * 读取认证信息
     * 通过请求头中的 token 判断是不是我们系统颁发的，并判断token有没有过期
     * 如果根据 accessToken 未找到用户信息，或者 accessToken 已过期，就抛出 401
     * @param accessToken
     * @return
     */
    Authentication readAuthentication(String accessToken);
}
