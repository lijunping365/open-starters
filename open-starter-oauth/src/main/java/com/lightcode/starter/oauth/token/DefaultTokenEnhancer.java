package com.lightcode.starter.oauth.token;

import com.lightcode.starter.oauth.authentication.Authentication;

/**
 * @author lijunping on 2022/3/31
 */
public class DefaultTokenEnhancer implements TokenEnhancer{

    @Override
    public AccessToken enhance(AccessToken accessToken, Authentication authentication) {
        return accessToken;
    }
}
