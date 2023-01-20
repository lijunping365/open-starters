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
package com.saucesubfresh.starter.oauth.token.support.redis;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.exception.InvalidRefreshTokenException;
import com.saucesubfresh.starter.oauth.properties.OAuthProperties;
import com.saucesubfresh.starter.oauth.properties.token.TokenProperties;
import com.saucesubfresh.starter.oauth.token.AbstractTokenStore;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis token store，把 token 及其映射存储到 Redis 中
 *
 * @author lijunping
 */
@Slf4j
public class RedisTokenStore extends AbstractTokenStore {
    private static final String REDIS_REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final OAuthProperties oauthProperties;

    public RedisTokenStore(TokenEnhancer tokenEnhancer, RedisTemplate<String, Object> redisTemplate, OAuthProperties oauthProperties) {
        super(tokenEnhancer, oauthProperties);
        this.redisTemplate = redisTemplate;
        this.oauthProperties = oauthProperties;
    }

    @Override
    protected AccessToken doGenerateToken(Authentication authentication) {
        UserDetails userDetails = authentication.getUserDetails();
        final TokenProperties tokenProperties = oauthProperties.getToken();
        AccessToken token = new AccessToken();
        String accessToken = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        long accessTokenExpiredTime = getAccessTokenExpiredTime(now);
        token.setAccessToken(accessToken);
        token.setExpiredTime(String.valueOf(accessTokenExpiredTime));
        long accessTokenExpiresIn = tokenProperties.getAccessTokenExpiresIn();
        redisTemplate.opsForValue().set(getAccessTokenKey(accessToken), userDetails, accessTokenExpiresIn, TimeUnit.SECONDS);

        if (supportRefreshToken()){
            String refreshToken = UUID.randomUUID().toString();
            token.setRefreshToken(refreshToken);
            long refreshTokenExpireTimes = tokenProperties.getRefreshTokenExpireTimes();
            long refreshTokenExpiredTime = accessTokenExpiresIn * refreshTokenExpireTimes;
            redisTemplate.opsForValue().set(getRefreshTokenKey(refreshToken), userDetails, refreshTokenExpiredTime, TimeUnit.SECONDS);
        }

        return token;
    }

    @Override
    public Authentication readAuthentication(String refreshToken) {
        Object o = redisTemplate.opsForValue().get(getRefreshTokenKey(refreshToken));
        if (Objects.isNull(o)){
            throw new InvalidRefreshTokenException("RefreshToken error or refreshToken has been invalid");
        }
        return new Authentication((UserDetails) o);
    }

    @Override
    public void invalidateAccessToken(String accessToken) {
        redisTemplate.delete(getAccessTokenKey(accessToken));
    }

    @Override
    public void invalidateRefreshToken(String refreshToken) {
        redisTemplate.delete(getRefreshTokenKey(refreshToken));
    }

    private String getAccessTokenKey(String accessToken){
        return oauthProperties.getToken().getRedisAccessTokenKey() + accessToken;
    }

    private String getRefreshTokenKey(String refreshToken){
        return REDIS_REFRESH_TOKEN_PREFIX + refreshToken;
    }
}
