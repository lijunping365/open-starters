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
package com.saucesubfresh.starter.security.service.support;

import com.saucesubfresh.starter.security.domain.Authentication;
import com.saucesubfresh.starter.security.domain.UserDetails;
import com.saucesubfresh.starter.security.exception.SecurityException;
import com.saucesubfresh.starter.security.properties.SecurityProperties;
import com.saucesubfresh.starter.security.service.TokenService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @author lijunping
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
            throw new SecurityException("AccessToken error or accessToken has been invalid");
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
