/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.oauth.config;

import com.openbytecode.starter.oauth.component.AuthenticationFailureHandler;
import com.openbytecode.starter.oauth.component.AuthenticationSuccessHandler;
import com.openbytecode.starter.oauth.component.DefaultAuthenticationFailureHandler;
import com.openbytecode.starter.oauth.component.DefaultAuthenticationSuccessHandler;
import com.openbytecode.starter.oauth.token.support.jwt.JwtTokenStore;
import com.openbytecode.starter.oauth.core.password.PasswordAuthenticationProcessor;
import com.openbytecode.starter.oauth.core.sms.SmsMobileAuthenticationProcessor;
import com.openbytecode.starter.oauth.properties.OAuthProperties;
import com.openbytecode.starter.oauth.service.UserDetailService;
import com.openbytecode.starter.oauth.token.DefaultTokenEnhancer;
import com.openbytecode.starter.oauth.token.TokenEnhancer;
import com.openbytecode.starter.oauth.token.TokenStore;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自动配置
 *
 * @author lijunping
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
    @ConditionalOnMissingBean
    public TokenEnhancer tokenEnhancer(){
        return new DefaultTokenEnhancer();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(TokenEnhancer tokenEnhancer){
        return new JwtTokenStore(tokenEnhancer, oAuthProperties);
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
