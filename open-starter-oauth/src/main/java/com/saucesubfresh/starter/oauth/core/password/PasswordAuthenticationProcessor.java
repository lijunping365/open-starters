package com.saucesubfresh.starter.oauth.core.password;

import com.saucesubfresh.starter.oauth.component.AuthenticationFailureHandler;
import com.saucesubfresh.starter.oauth.component.AuthenticationSuccessHandler;
import com.saucesubfresh.starter.oauth.core.AbstractAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.enums.OAuthExceptionEnum;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.request.PasswordLoginRequest;
import com.saucesubfresh.starter.oauth.service.UserDetailService;
import com.saucesubfresh.starter.oauth.token.TokenStore;
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
    protected UserDetails loadUserDetails(PasswordLoginRequest request) throws AuthenticationException{
        final UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());
        if (Objects.isNull(userDetails)){
            throw new AuthenticationException(OAuthExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
        if (!matches){
            throw new AuthenticationException(OAuthExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return userDetails;
    }
}
