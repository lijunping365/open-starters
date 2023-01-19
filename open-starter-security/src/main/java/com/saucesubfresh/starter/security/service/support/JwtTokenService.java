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
import com.saucesubfresh.starter.security.exception.InvalidBearerTokenException;
import com.saucesubfresh.starter.security.properties.SecurityProperties;
import com.saucesubfresh.starter.security.service.TokenService;
import com.saucesubfresh.starter.security.utils.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Objects;

/**
 * @author lijunping
 */
public class JwtTokenService implements TokenService {
    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";

    private final SecurityProperties securityProperties;

    public JwtTokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication readAuthentication(String accessToken) {
        String subject;
        Object tokenType;
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(securityProperties.getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            subject = claims.getSubject();
            tokenType = claims.get(TOKEN_TYPE);
        }catch (Exception e){
            throw new InvalidBearerTokenException("AccessToken error or accessToken has been invalid");
        }

        if (!Objects.equals(tokenType, ACCESS_TOKEN)){
            throw new InvalidBearerTokenException("AccessToken error or accessToken has been invalid");
        }

        UserDetails userDetails = JSON.parse(subject, UserDetails.class);
        Authentication authentication = new Authentication();
        authentication.setUserDetails(userDetails);
        return authentication;
    }
}
