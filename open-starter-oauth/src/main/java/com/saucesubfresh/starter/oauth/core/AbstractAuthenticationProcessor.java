package com.saucesubfresh.starter.oauth.core;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.component.AuthenticationFailureHandler;
import com.saucesubfresh.starter.oauth.component.AuthenticationSuccessHandler;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.enums.OAuthExceptionEnum;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.request.BaseLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenStore;
import com.saucesubfresh.starter.oauth.enums.OAuthExceptionEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author lijunping on 2021/12/8
 */
@Slf4j
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
    public AccessToken authentication(T request){
        try {
            UserDetails userDetails = loadUserDetails(request);
            checkAccountLock(userDetails);
            Authentication authentication = new Authentication(userDetails);
            AccessToken accessToken = tokenStore.generateToken(authentication);
            authenticationSuccessHandler.onAuthenticationSuccess(authentication);
            return accessToken;
        } catch (AuthenticationException e){
            authenticationFailureHandler.onAuthenticationFailureHandler(e);
            throw new AuthenticationException(e.getCode(), e.getMessage());
        }
    }

    private void checkAccountLock(UserDetails userDetails){
        if (Objects.nonNull(userDetails) && userDetails.getAccountLocked()) {
            log.info("账号已被锁定 {}", userDetails);
            throw new AuthenticationException(OAuthExceptionEnum.ACCOUNT_LOCKED);
        }
    }

    protected abstract UserDetails loadUserDetails(T request);

}
