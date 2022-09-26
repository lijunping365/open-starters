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
package com.saucesubfresh.starter.security.authentication;

import com.saucesubfresh.starter.security.context.UserSecurityContext;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import com.saucesubfresh.starter.security.domain.Authentication;
import com.saucesubfresh.starter.security.enums.SecurityExceptionEnum;
import com.saucesubfresh.starter.security.exception.SecurityException;
import com.saucesubfresh.starter.security.service.TokenService;
import com.saucesubfresh.starter.security.utils.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lijunping
 */
@Slf4j
public class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint{

    private final TokenService tokenService;

    public BearerTokenAuthenticationEntryPoint(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void commence(HttpServletRequest request) throws SecurityException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new SecurityException(SecurityExceptionEnum.NON_AUTHENTICATION);
        }
        String accessToken = header.substring(7);
        Authentication authentication = tokenService.readAuthentication(accessToken);
        UserSecurityContext user = JSON.parse(authentication.getUserDetails(), UserSecurityContext.class);
        UserSecurityContextHolder.setContext(user);
    }
}
