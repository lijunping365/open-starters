package com.saucesubfresh.starter.oauth.token;

import com.saucesubfresh.starter.oauth.authentication.Authentication;

/**
 * @author lijunping on 2022/3/31
 */
public abstract class AbstractTokenStore implements TokenStore{

    private final TokenEnhancer tokenEnhancer;

    public AbstractTokenStore(TokenEnhancer tokenEnhancer) {
        this.tokenEnhancer = tokenEnhancer;
    }

    @Override
    public AccessToken generateToken(Authentication authentication) {
        AccessToken result = doGenerateToken(authentication);
        result = tokenEnhancer.enhance(result, authentication);
        return result;
    }

    public abstract AccessToken doGenerateToken(Authentication authentication);
}
