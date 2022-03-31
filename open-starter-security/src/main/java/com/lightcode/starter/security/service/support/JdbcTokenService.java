package com.lightcode.starter.security.service.support;

import com.lightcode.starter.security.domain.Authentication;
import com.lightcode.starter.security.service.TokenService;

/**
 * @author lijunping on 2022/3/31
 */
public class JdbcTokenService implements TokenService {

    @Override
    public Authentication readAuthentication(String accessToken) {
        return null;
    }
}
