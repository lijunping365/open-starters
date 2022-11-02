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
package com.saucesubfresh.starter.oauth.core.sms;

import com.saucesubfresh.starter.oauth.component.AuthenticationFailureHandler;
import com.saucesubfresh.starter.oauth.component.AuthenticationSuccessHandler;
import com.saucesubfresh.starter.oauth.core.AbstractAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.exception.InvalidArgumentException;
import com.saucesubfresh.starter.oauth.exception.UsernameNotFoundException;
import com.saucesubfresh.starter.oauth.request.MobileLoginRequest;
import com.saucesubfresh.starter.oauth.service.UserDetailService;
import com.saucesubfresh.starter.oauth.token.TokenStore;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author lijunping
 */
public class SmsMobileAuthenticationProcessor extends AbstractAuthenticationProcessor<MobileLoginRequest> {

    private final UserDetailService userDetailService;

    public SmsMobileAuthenticationProcessor(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler, TokenStore tokenStore, UserDetailService userDetailService) {
        super(authenticationSuccessHandler, authenticationFailureHandler, tokenStore);
        this.userDetailService = userDetailService;
    }

    @Override
    protected UserDetails loadUserDetails(MobileLoginRequest request) throws AuthenticationException{
        if (StringUtils.isBlank(request.getMobile())){
            throw new InvalidArgumentException("Mobile must not be empty or null");
        }

        final UserDetails userDetails = userDetailService.loadUserByMobile(request.getMobile());
        if (Objects.isNull(userDetails)){
            throw new UsernameNotFoundException("User not found:" + request.getMobile());
        }
        return userDetails;
    }
}
