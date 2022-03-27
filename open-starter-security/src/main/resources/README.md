
小提示：

1 需要将我们的 SecurityInterceptor 添加到 Spring 的拦截器链上

```java
/**
 * @author lijunping on 2022/1/11
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;

    private final SecurityInterceptor securityInterceptor;

    public SecurityConfig(SecurityProperties securityProperties, SecurityInterceptor securityInterceptor) {
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
```

2 启用注解

@EnableSecurity

文档后续会补充

