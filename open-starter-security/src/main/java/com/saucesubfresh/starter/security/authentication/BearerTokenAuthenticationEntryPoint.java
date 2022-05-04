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
 * @author: 李俊平
 * @Date: 2022-05-04 09:33
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
