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
package com.saucesubfresh.starter.oauth.core;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.component.AuthenticationFailureHandler;
import com.saucesubfresh.starter.oauth.component.AuthenticationSuccessHandler;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.request.BaseLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenStore;

import java.util.Objects;

/**
 * @author lijunping
 */
public abstract class AbstractAuthenticationProcessor<T extends BaseLoginRequest> implements AuthenticationProcessor<T>{

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final TokenStore tokenStore;

    public AbstractAuthenticationProcessor(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler, TokenStore tokenStore) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.tokenStore = tokenStore;
    }

    @Override
    public AccessToken authentication(T request) throws AuthenticationException{
        try {
            UserDetails userDetails = loadUserDetails(request);
            checkAccountLock(userDetails);
            Authentication authentication = new Authentication(userDetails);
            AccessToken accessToken = tokenStore.generateToken(authentication);
            authenticationSuccessHandler.onAuthenticationSuccess(authentication);
            return accessToken;
        } catch (AuthenticationException e){
            authenticationFailureHandler.onAuthenticationFailureHandler(e);
            throw new AuthenticationException(e.getMessage());
        }
    }

    private void checkAccountLock(UserDetails userDetails) {
        if (Objects.nonNull(userDetails) && userDetails.getAccountLocked()) {
            throw new AuthenticationException("This account is locked:" + userDetails.getId());
        }
    }

    protected abstract UserDetails loadUserDetails(T request) throws AuthenticationException;

}
