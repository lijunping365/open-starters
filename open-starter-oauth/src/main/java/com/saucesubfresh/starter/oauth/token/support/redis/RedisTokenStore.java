package com.saucesubfresh.starter.oauth.token.support.redis;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.properties.OAuthProperties;
import com.saucesubfresh.starter.oauth.properties.token.TokenProperties;
import com.saucesubfresh.starter.oauth.token.AbstractTokenStore;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: Redis token store，把 token 及其映射存储到 Redis 中
 */
@Slf4j
public class RedisTokenStore extends AbstractTokenStore {

    private final RedisTemplate<String, Object> redisTemplate;
    private final OAuthProperties oauthProperties;

    public RedisTokenStore(TokenEnhancer tokenEnhancer, RedisTemplate<String, Object> redisTemplate, OAuthProperties oauthProperties) {
        super(tokenEnhancer);
        this.redisTemplate = redisTemplate;
        this.oauthProperties = oauthProperties;
    }

    @Override
    public AccessToken doGenerateToken(Authentication authentication) {
        UserDetails userDetails = authentication.getUserDetails();
        final TokenProperties tokenProperties = oauthProperties.getToken();
        AccessToken token = new AccessToken();
        String accessToken = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        long expiredTime = now + tokenProperties.getAccessTokenExpiresIn() * 1000;
        token.setExpiredTime(String.valueOf(expiredTime));
        token.setAccessToken(accessToken);
        redisTemplate.opsForValue().set(buildAccessTokenKey(accessToken), userDetails, tokenProperties.getAccessTokenExpiresIn(), TimeUnit.SECONDS);
        return token;
    }

    private String buildAccessTokenKey(String accessToken){
        return oauthProperties.getToken().getTokenPrefix() + accessToken;
    }
}
