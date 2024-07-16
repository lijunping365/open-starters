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
    <groupId>com.openbytecode</groupId>
    <artifactId>open-starter-oauth</artifactId>
    <version>1.0.5</version>
</dependency>
```

### 2. 配置相关参数

```yaml
com:
  openbytecode:
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

## 常见问题

### 1. 如何区分认证和授权

- 用户认证：

所谓用户认证(Authentication)，就是让用户登录，并且在接下来的一段时间内让用户访问网站时可以使用其携带的用户信息，而不需要再次登录的机制。

注意：用户通过Web表单将自己的用户名和密码发送到服务器的接口。这一过程一般是一个HTTP POST请求。建议通过SSL加密传输(https协议)，从而避免敏感信息被嗅探。

- 用户授权

用户授权指的是规定并允许用户使用自己的权限，例如发布帖子、管理站点等。

### 2. 关于 JWT token 和 Redis Token 的选择问题

- jwt token

优点：验证速度快，不需要服务器存储 accessToken 和 refreshToken

缺点：无法强制失效，一旦生成只要没有过期就可以用

适用场景：不需要频繁修改用户的信息

- redis token

优点：可以使 token 强制失效（通过中间层，在生成用户token 之后记录 【uid：accessToken，refreshToken】对应关系，在强制失效时通过 uid 删除对应的 accessToken 和 refreshToken）

缺点：验证速度相比 jwt token 慢，需要服务器存储 accessToken 和 refreshToken

适用场景：当管理员修改了用户的权限或修改了用户的锁定状态用户可以立即感触到。

可以根据使用场景及各自优缺点进行选择。

### 3. 用户可以直接拿 refreshToken 当 accessToken 使用吗

当用户把 refreshToken 当作 accessToken 来使用时在认证时是不会通过的。

对于 jwt token 来说，其实用户拿到的 accessToken 和 refreshToken 是有区分 token 类型字段的

对于 redis token 来说，accessToken 和 refreshToken 是使用不同的 redis key 前缀去存储的。

### 4. 如何使用 access token 自动续期功能？

1. 在 security 验证 accessToken 的有效性时，当 accessToken 失效返回特定的异常

2. 前端在接收到这个异常之后，发请求给 oauth 表明要 refreshToken， oauth 检查 refreshToken 是否失效，如果未失效就重新生成 accessToken 和 refreshToken 返回给客户端，否则告诉客户端你需要重新登录。

3. 只要 refreshToken 没有失效就可以一直为用户失效的 accessToken 续期，一旦 refreshToken 失效，那么用户就需要重新登录。

### 5. 如何使 accessToken 强制失效？

1. 首先，accessToken 强制失效只支持 redis token，也就是说你必须要使用 redis token

2. 该 starter 已提供删除 accessToken 接口，开发者只需要在生成用户 accessToken 之后记录【uid：accessToken，refreshToken】对应关系，

3. 在想要强制失效的时候，通过 uid 拿到该用户的 accessToken 和 refreshToken 调用该 starter 的删除

## 注意事项

### 1. Oauth 组件通常与 Security 组件配合使用

在用户登录成功之后，我们会给用户颁发令牌（Token），令牌关联着用户信息

该插件提供多种令牌的生成及存储策略，但是不提供令牌的查询操作，令牌的校验操作在 [open-starter-security]

插件中提供了实现，这样做的原因就是考虑到如果是在微服务环境下其他微服务可以方便的根据令牌获取用户信息

假设有如下微服务

> A 服务是认证系统，集成了 open-starter-oauth 插件，负责给用户颁发令牌
>
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

### 2. Oauth 组件与 Security 组件配置须一致

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

### 1.0.4

1. 新增自动为 token 续期，即自动刷新 token 能力

### 1.0.5

认证成功处理和认证失败处理添加请求参数字段，方便在处理时获取关键请求信息