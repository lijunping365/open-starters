package com.saucesubfresh.starter.oauth.core.sms;

import com.saucesubfresh.starter.oauth.component.AuthenticationFailureHandler;
import com.saucesubfresh.starter.oauth.component.AuthenticationSuccessHandler;
import com.saucesubfresh.starter.oauth.core.AbstractAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.enums.OAuthExceptionEnum;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.request.MobileLoginRequest;
import com.saucesubfresh.starter.oauth.service.UserDetailService;
import com.saucesubfresh.starter.oauth.token.TokenStore;

import java.util.Objects;

/**
 * @author lijunping on 2021/12/8
 */
public class SmsMobileAuthenticationProcessor extends AbstractAuthenticationProcessor<MobileLoginRequest> {

    private final UserDetailService userDetailService;

    public SmsMobileAuthenticationProcessor(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler, TokenStore tokenStore, UserDetailService userDetailService) {
        super(authenticationSuccessHandler, authenticationFailureHandler, tokenStore);
        this.userDetailService = userDetailService;
    }

    @Override
    protected UserDetails loadUserDetails(MobileLoginRequest request) throws AuthenticationException{
        final UserDetails userDetails = userDetailService.loadUserByMobile(request.getMobile());
        if (Objects.isNull(userDetails)){
            throw new AuthenticationException(OAuthExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return userDetails;
    }
}
