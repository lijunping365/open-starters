package com.lightcode.starter.oauth.core.password;

import com.lightcode.starter.oauth.component.AuthenticationFailureHandler;
import com.lightcode.starter.oauth.component.AuthenticationSuccessHandler;
import com.lightcode.starter.oauth.core.AbstractAuthenticationProcessor;
import com.lightcode.starter.oauth.domain.UserDetails;
import com.lightcode.starter.oauth.enums.ResultEnum;
import com.lightcode.starter.oauth.exception.AuthenticationException;
import com.lightcode.starter.oauth.request.PasswordLoginRequest;
import com.lightcode.starter.oauth.service.UserDetailService;
import com.lightcode.starter.oauth.token.TokenStore;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author lijunping on 2021/12/8
 */
public class PasswordAuthenticationProcessor extends AbstractAuthenticationProcessor<PasswordLoginRequest> {

    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordAuthenticationProcessor(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler, TokenStore tokenStore, UserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        super(authenticationSuccessHandler, authenticationFailureHandler, tokenStore);
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UserDetails loadUserDetails(PasswordLoginRequest request){
        final UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername(), request.getUserType());
        if (Objects.isNull(userDetails)){
            throw new AuthenticationException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
        if (!matches){
            throw new AuthenticationException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return userDetails;
    }
}
