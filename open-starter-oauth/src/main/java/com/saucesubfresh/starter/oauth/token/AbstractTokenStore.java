/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.oauth.token;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.properties.OAuthProperties;
import com.saucesubfresh.starter.oauth.properties.token.TokenProperties;

/**
 * @author lijunping
 */
public abstract class AbstractTokenStore implements TokenStore{
    /**
     * Seconds to milliseconds
     */
    private static final long UNIT = 1000;
    private final TokenEnhancer tokenEnhancer;
    private final OAuthProperties oauthProperties;

    public AbstractTokenStore(TokenEnhancer tokenEnhancer, OAuthProperties oauthProperties) {
        this.tokenEnhancer = tokenEnhancer;
        this.oauthProperties = oauthProperties;
    }

    @Override
    public AccessToken generateToken(Authentication authentication) {
        AccessToken result = doGenerateToken(authentication);
        result = tokenEnhancer.enhance(result, authentication);
        return result;
    }

    @Override
    public AccessToken refreshToken(String refreshToken) {
        Authentication authentication = readAuthentication(refreshToken);
        return generateToken(authentication);
    }

    /**
     * 获取 accessToken 失效时间（时间戳）
     */
    protected long getAccessTokenExpiredTime(long now){
        final TokenProperties tokenProperties = oauthProperties.getToken();
        long accessTokenExpiresIn = tokenProperties.getAccessTokenExpiresIn() * UNIT;
        return now + accessTokenExpiresIn;
    }

    /**
     * 获取 refreshToken 失效时间（时间戳）
     */
    protected long getRefreshTokenExpiredTime(long now){
        final TokenProperties tokenProperties = oauthProperties.getToken();
        long refreshTokenExpiresIn = tokenProperties.getAccessTokenExpiresIn() * tokenProperties.getRefreshTokenExpireTimes() * UNIT;
        return now + refreshTokenExpiresIn;
    }

    /**
     * 是否支持刷新 token
     */
    protected boolean supportRefreshToken(){
        return oauthProperties.getToken().isSupportRefreshToken();
    }

    /**
     * 根据
     * @param authentication
     * @return
     */
    public abstract AccessToken doGenerateToken(Authentication authentication);

    /**
     * 通过 refreshToken 获取用户信息
     * @param refreshToken
     * @return
     */
    protected abstract Authentication readAuthentication(String refreshToken);
}
