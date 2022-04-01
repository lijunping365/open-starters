package com.lightcode.starter.security.service.support;

import com.lightcode.starter.security.exception.SecurityException;
import com.lightcode.starter.security.domain.Authentication;
import com.lightcode.starter.security.domain.UserDetails;
import com.lightcode.starter.security.properties.SecurityProperties;
import com.lightcode.starter.security.service.TokenService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @author lijunping on 2022/3/31
 */
public class RedisTokenService implements TokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityProperties securityProperties;

    public RedisTokenService(RedisTemplate<String, Object> redisTemplate, SecurityProperties securityProperties) {
        this.redisTemplate = redisTemplate;
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication readAuthentication(String accessToken){
        Object o = redisTemplate.opsForValue().get(buildAccessTokenKey(accessToken));
        if (Objects.isNull(o)){
            throw new SecurityException("AccessToken 错误或 AccessToken 已失效");
        }
        UserDetails userDetails = (UserDetails) o;
        Authentication authentication = new Authentication();
        authentication.setUserDetails(userDetails);
        return authentication;
    }

    private String buildAccessTokenKey(String accessToken){
        return securityProperties.getTokenPrefix() + accessToken;
    }
}
