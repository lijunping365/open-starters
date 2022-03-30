package com.lightcode.starter.oauth.config;

import com.lightcode.starter.oauth.component.AuthenticationFailureHandler;
import com.lightcode.starter.oauth.component.AuthenticationSuccessHandler;
import com.lightcode.starter.oauth.component.DefaultAuthenticationFailureHandler;
import com.lightcode.starter.oauth.component.DefaultAuthenticationSuccessHandler;
import com.lightcode.starter.oauth.core.password.PasswordAuthenticationProcessor;
import com.lightcode.starter.oauth.core.sms.SmsMobileAuthenticationProcessor;
import com.lightcode.starter.oauth.properties.OAuthProperties;
import com.lightcode.starter.oauth.service.UserDetailService;
import com.lightcode.starter.oauth.token.TokenStore;
import com.lightcode.starter.oauth.token.support.jwt.JwtTokenStore;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 安全相关配置
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthAutoConfiguration {

    private final OAuthProperties oAuthProperties;

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new DefaultAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    @ConditionalOnExpression
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(oAuthProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserDetailService.class)
    public PasswordAuthenticationProcessor passwordAuthenticationProcessor(AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                           AuthenticationFailureHandler authenticationFailureHandler,
                                                                           UserDetailService userDetailService,
                                                                           PasswordEncoder passwordEncoder,
                                                                           TokenStore tokenStore){
        return new PasswordAuthenticationProcessor(authenticationSuccessHandler, authenticationFailureHandler, tokenStore, userDetailService, passwordEncoder);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserDetailService.class)
    public SmsMobileAuthenticationProcessor smsMobileAuthenticationProcessor(AuthenticationSuccessHandler authenticationSuccessHandler,
                                                                             AuthenticationFailureHandler authenticationFailureHandler,
                                                                             UserDetailService userDetailService,
                                                                             TokenStore tokenStore){
        return new SmsMobileAuthenticationProcessor(authenticationSuccessHandler, authenticationFailureHandler, tokenStore, userDetailService);
    }
}
