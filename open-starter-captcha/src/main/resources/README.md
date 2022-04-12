# 验证码生成和校验插件

## 功能

- [x] 生成图片验证码，图片验证码的长度、宽度、验证码长度、验证码有效期可配置

- [x] 生成短信验证码，验证码长度、验证码有效期可配置

- [x] 生成登录二维码，二维码的长度、宽度、二维码外边距、验证码有效期可配置

- [x] 生成后的验证码默认会存储到 Redis 上，而且会根据验证码的有效期自动失效

- [x] 验证码的发送由开发者实现，这里提供了回调函数。

- [x] 可传入与生成验证码时传入的 requestId 和用户输入的验证码进行验证码的校验

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-captcha</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置验证码相关参数

```yaml
com:
  saucesubfresh:
    captcha:
      image:
        length: 4 #图形验证码的位数为6，将覆盖默认配置中的4
        width: 200 #图形验证码的宽度为100，将覆盖默认配置中的67，但由于请求中设置的宽度为200，所以真正的宽度将为200
        height: 60
      scan:
        width: 300 #二维码宽度
        height: 300 #二维码高度
        margin: 1 #二维码外边距，0到4  
```

### 3. 生成验证码并发送

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
    private HttpServletResponse response;
    /**
     * 创建验证码
     */
    @PostMapping("/validate/code")
    public void createCode(@RequestBody @Valid OpenJobCaptchaRequest request) throws Exception {
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getRequestId());
        captchaGenerateRequest.setType(request.getType());
        
        captchaProcessor.create(captchaGenerateRequest, validateCode -> {
            final String type = request.getType();
            final ValidateCodeType codeType = ValidateCodeType.getValidateCodeType(type);
            switch (codeType){
                case IMAGE:
                    try {
                        ImageValidateCode imageValidateCode = (ImageValidateCode) validateCode;
                        // 输出图片
                        ImageIO.write(imageValidateCode.getImage(), "JPEG", response.getOutputStream());
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    break;
                case SMS:
                    log.info("向手机号: {}发送短信验证码: {}", request.getMobile(), validateCode.getCode());
                    break;
            }
        });
    }
}
```

## 扩展示例

### 1. 替换原有的验证码生成方式

比如要替换掉原来的短信验证码生成方式

```java

@Component("smsCodeGenerator")
public class XxxCodeGenerator implements ValidateCodeGenerator {

    private final CaptchaProperties captchaProperties;

    public XxxCodeGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    @Override
    public XxxValidateCode generate() throws Exception {
        String code = RandomStringUtils.randomNumeric(captchaProperties.getSms().getLength());
        return new ValidateCode(code, captchaProperties.getSms().getExpireTime());
    }
}
```


### 2. 扩展验证码存储方式（CaptchaRepository）

```java
@Component
public class XxxCaptchaRepository implements CaptchaRepository {

  @Override
  public <C extends ValidateCode> void save(String requestId, C code) {
    // TODO
  }

  @Override
  public String get(String requestId) {
    // TODO
    return null;
  }
}
```


## 注意点

### 1. 为什么不把发送逻辑也默认实现了

> 1. 发送图片验证码需要 HttpServletResponse，这个写在这里面是发不出的

> 2. 短信服务商需要根据自身去决定