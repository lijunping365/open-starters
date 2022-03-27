package com.lightcode.starter.security.service;


import com.lightcode.starter.security.domain.Authentication;

/**
 * @author lijunping on 2021/10/22
 */
public interface TokenService {

    /**
     * 通过 accessToken 获取 Authentication
     * @param accessToken
     * @return
     */
    Authentication readAuthentication(String accessToken);
}
