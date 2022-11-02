# 认证插件

包括登录认证以及颁发令牌 token

## 功能

- [x] 用户名密码登录，登录成功后给用户颁发令牌

- [x] 手机号短信验证码登录，登录成功后给用户颁发令牌

- [x] 令牌的生成及存储支持三种方式：jwt、redis、jdbc

- [x] 支持令牌在返回时增强机制

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-oauth</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. 配置相关参数

```yaml
com:
  saucesubfresh:
    oauth:
      token:
        # token 有效期，单位 秒
        accessTokenExpiresIn: 7200
        # jwt secretKey，注意长度，注意我们使用的是 SignatureAlgorithm.HS256，所以 secretKey 经过 Base64 编码后的长度 * 8 必须大于 256
        secretKey: ThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKey
```

### 3. 实现 UserDetailService 接口

节选自 Open-Job
```java
@Service
public class OpenJobUserServiceImpl extends ServiceImpl<OpenJobUserMapper, OpenJobUserDO> implements OpenJobUserService, UserDetailService {

    @Autowired
    private OpenJobUserMapper openJobUserMapper;
    
    @Override
    public OpenJobUserRespDTO loadUserByUserId(Long userId) {
        OpenJobUserDO openJobUserDO = this.openJobUserMapper.selectById(userId);
        return OpenJobUserConvert.INSTANCE.convert(openJobUserDO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        OpenJobUserDO openJobUserDO = this.openJobUserMapper.loadUserByUsername(username);
        return OpenJobUserConvert.INSTANCE.convertDetails(openJobUserDO);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        OpenJobUserDO openJobUserDO = this.openJobUserMapper.loadUserByMobile(mobile);
        return OpenJobUserConvert.INSTANCE.convertDetails(openJobUserDO);
    }
}
```

### 4. 开始登录颁发令牌

节选自 Open-Job
```java

@Slf4j
@Validated
@RestController
@RequestMapping("/openJobLogin")
public class OpenJobLoginController {

    @Autowired
    private CaptchaProcessor captchaProcessor;

    @Autowired
    private PasswordAuthenticationProcessor passwordAuthentication;

    @Autowired
    private SmsMobileAuthenticationProcessor smsMobileAuthentication;

    /**
     * 用户名密码登录
     * @param request
     * @return
     */
    @PostMapping("/account")
    public Result<AccessToken> loginByUsername(@RequestBody @Valid OpenJobPasswordLoginRequest request){
        CaptchaVerifyRequest captchaVerifyRequest = new CaptchaVerifyRequest()
                .setRequestId(request.getDeviceId())
                .setCode(request.getCaptcha());
        try {
            captchaProcessor.validate(captchaVerifyRequest);
        } catch (ValidateCodeException e){
            throw new ControllerException(e.getMessage());
        }

        PasswordLoginRequest passwordLoginRequest = new PasswordLoginRequest()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword());
        try {
            final AccessToken accessToken = passwordAuthentication.authentication(passwordLoginRequest);
            return Result.succeed(accessToken);
        } catch (AuthenticationException e){
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 手机号短信验证码登录
     * @param request
     * @return
     */
    @PostMapping("/mobile")
    public Result<AccessToken> loginByMobile(@RequestBody @Valid OpenJobMobileLoginRequest request){
        CaptchaVerifyRequest captchaVerifyRequest = new CaptchaVerifyRequest()
                .setRequestId(request.getDeviceId())
                .setCode(request.getCaptcha());
        try {
            captchaProcessor.validate(captchaVerifyRequest);
        } catch (ValidateCodeException e){
            throw new ControllerException(e.getMessage());
        }

        MobileLoginRequest mobileLoginRequest = new MobileLoginRequest().setMobile(request.getMobile());
        try {
            final AccessToken accessToken = smsMobileAuthentication.authentication(mobileLoginRequest);
            return Result.succeed(accessToken);
        } catch (AuthenticationException e){
            throw new ControllerException(e.getMessage());
        }
    }
}

```

注意区分认证和授权

1. 用户认证：

所谓用户认证(Authentication)，就是让用户登录，并且在接下来的一段时间内让用户访问网站时可以使用其携带的用户信息，而不需要再次登录的机制。

注意：用户通过Web表单将自己的用户名和密码发送到服务器的接口。这一过程一般是一个HTTP POST请求。建议通过SSL加密传输(https协议)，从而避免敏感信息被嗅探。

2. 用户授权

用户授权指的是规定并允许用户使用自己的权限，例如发布帖子、管理站点等。

## 扩展示例

## 重要说明

在用户登录成功之后，我们会给用户颁发令牌（Token），令牌关联着用户信息

该插件提供多种令牌的生成及存储策略，但是不提供令牌的查询操作，令牌的校验操作在 [open-starter-security]

插件中提供了实现，这样做的原因就是考虑到如果是在微服务环境下其他微服务可以方便的根据令牌获取用户信息

假设有如下微服务

> A 服务是认证系统，集成了 open-starter-oauth 插件，负责给用户颁发令牌

> B 服务是支付系统，集成了 open-starter-security 插件，负责认证用户传过来的令牌，目的是为了根据用户传来的令牌获取用户信息（令牌的校验解析操作）

但是需要注意的地方就是 open-starter-oauth 和 open-starter-security 注入的令牌生成校验策略须一致

例如 open-starter-oauth 注入的是 JwtTokenStore

```java
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthAutoConfiguration {

    private final OAuthProperties oAuthProperties;

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore(TokenEnhancer tokenEnhancer){
        return new JwtTokenStore(tokenEnhancer, oAuthProperties);
    }
}
```

那么 open-starter-security 注入的也需要是 Jwt 相关的 JwtTokenService

```java
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TokenService tokenService(SecurityProperties securityProperties){
        return new JwtTokenService(securityProperties);
    }

}
```

如果 open-starter-oauth 注入的是 RedisTokenStore，那么 open-starter-security 注入的也必须是 RedisTokenService

## 注意点

> 1.如果采用的是 Jwt token，配置 jwt 的 secretKey 时，open-starter-oauth 和 open-starter-security 须一致

> 2.如果采用的是 redis token，配置 redis 的 tokenPrefix 时，open-starter-oauth 和 open-starter-security 须一致，而且 redis 地址也应一致

## 版本更新说明

### 1.0.1 

1. 声明异常抛出类型

### 1.0.2 

1. 增加角色列表字段

2. token 过期时间改为 long 型

3. 配置优化，使用了 @NestedConfigurationProperty，配置时有提示

### 1.0.3

1. 优化抛出异常

2. 细化异常分类，可以针对不同的异常做出不同的处理