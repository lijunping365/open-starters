package com.saucesubfresh.starter.security.service.support;

import com.saucesubfresh.starter.security.enums.SecurityExceptionEnum;
import com.saucesubfresh.starter.security.exception.SecurityException;
import com.saucesubfresh.starter.security.domain.Authentication;
import com.saucesubfresh.starter.security.domain.UserDetails;
import com.saucesubfresh.starter.security.properties.SecurityProperties;
import com.saucesubfresh.starter.security.service.TokenService;
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
            throw new SecurityException(SecurityExceptionEnum.UNAUTHORIZED);
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
