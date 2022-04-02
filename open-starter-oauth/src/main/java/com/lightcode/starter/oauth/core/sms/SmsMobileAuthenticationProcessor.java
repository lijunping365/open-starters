package com.lightcode.starter.oauth.core.sms;

import com.lightcode.starter.oauth.component.AuthenticationFailureHandler;
import com.lightcode.starter.oauth.component.AuthenticationSuccessHandler;
import com.lightcode.starter.oauth.core.AbstractAuthenticationProcessor;
import com.lightcode.starter.oauth.domain.UserDetails;
import com.lightcode.starter.oauth.enums.OAuthExceptionEnum;
import com.lightcode.starter.oauth.exception.AuthenticationException;
import com.lightcode.starter.oauth.request.MobileLoginRequest;
import com.lightcode.starter.oauth.service.UserDetailService;
import com.lightcode.starter.oauth.token.TokenStore;

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
    protected UserDetails loadUserDetails(MobileLoginRequest request){
        final UserDetails userDetails = userDetailService.loadUserByMobile(request.getMobile());
        if (Objects.isNull(userDetails)){
            throw new AuthenticationException(OAuthExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return userDetails;
    }
}
