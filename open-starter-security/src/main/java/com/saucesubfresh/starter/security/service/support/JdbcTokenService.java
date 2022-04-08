package com.saucesubfresh.starter.security.service.support;

import com.saucesubfresh.starter.security.domain.Authentication;
import com.saucesubfresh.starter.security.service.TokenService;

/**
 * @author lijunping on 2022/3/31
 */
public class JdbcTokenService implements TokenService {

    @Override
    public Authentication readAuthentication(String accessToken) {
        return null;
    }
}
