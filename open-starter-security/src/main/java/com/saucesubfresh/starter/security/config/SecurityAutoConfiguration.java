package com.saucesubfresh.starter.security.config;

import com.saucesubfresh.starter.security.properties.SecurityProperties;
import com.saucesubfresh.starter.security.service.TokenService;
import com.saucesubfresh.starter.security.service.support.JwtTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@Import(SecurityConfiguration.class)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TokenService tokenService(SecurityProperties securityProperties){
        return new JwtTokenService(securityProperties);
    }

}
