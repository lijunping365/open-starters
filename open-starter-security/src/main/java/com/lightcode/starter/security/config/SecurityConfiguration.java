package com.lightcode.starter.security.config;

import com.lightcode.starter.security.interceptor.SecurityInterceptor;
import com.lightcode.starter.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lijunping on 2022/4/1
 */
@ConditionalOnBean(SecurityInterceptor.class)
public class SecurityConfiguration implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;
    private final SecurityInterceptor securityInterceptor;

    public SecurityConfiguration(SecurityProperties securityProperties, SecurityInterceptor securityInterceptor) {
        this.securityProperties = securityProperties;
        this.securityInterceptor = securityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .excludePathPatterns(securityProperties.getIgnorePaths())
                .excludePathPatterns(securityProperties.getDefaultIgnorePaths());
    }
}
