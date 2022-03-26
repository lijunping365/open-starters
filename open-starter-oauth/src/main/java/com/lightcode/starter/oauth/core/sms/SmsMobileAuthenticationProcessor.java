package com.lightcode.starter.oauth.core.sms;

import com.lightcode.starter.oauth.component.AuthenticationFailureHandler;
import com.lightcode.starter.oauth.component.AuthenticationSuccessHandler;
import com.lightcode.starter.oauth.core.AbstractAuthenticationProcessor;
import com.lightcode.starter.oauth.domain.UserDetails;
import com.lightcode.starter.oauth.request.MobileLoginRequest;
import com.lightcode.starter.oauth.service.UserDetailService;
import com.lightcode.starter.oauth.token.TokenStore;

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
        return userDetailService.loadUserByMobile(request.getMobile(), request.getUserType());
    }
}
