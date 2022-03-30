# 安全插件

既是一个安全插件又是一个用户信息存储小平台，关键是要根据 Token 去拿用户信息，用户信息是用户认证

微服务
A 服务是认证系统

集成 Oauth 插件，负责存 token 信息

B 服务是支付系统

Security 插件，负责读取 token 信息

C 服务是下单系统

Security 插件

## 功能

1. 配置路由白名单功能，白名单内的路由不会被拦截

2. 基于注解拦截进行权限校验，如果方法上加了 PreAuthorization 注解则会进行拦截并校验权限，如果有相应权限才能通过，否则将被拒绝访问


## 用例

### 1. 添加 Maven 依赖


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

2. 



2 启用注解

@EnableSecurity

文档后续会补充

