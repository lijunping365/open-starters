# 验证码生成和校验插件

## 功能

- [x] 生成图片验证码，图片验证码的长度、宽度、验证码长度、验证码有效期可配置

- [x] 生成短信验证码，验证码长度、验证码有效期可配置

- [x] 生成登录二维码，二维码的长度、宽度、二维码外边距、验证码有效期可配置

- [x] 生成后的验证码默认会存储到 Redis 上，而且会根据验证码的有效期自动失效

- [x] 验证码的发送由开发者实现

- [x] 可传入与生成验证码时传入的 requestId 和用户输入的验证码进行验证码的校验

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.openbytecode</groupId>
    <artifactId>open-starter-captcha</artifactId>
    <version>1.0.4</version>
</dependency>
```

### 2. 配置验证码相关参数

```yaml
com:
  openbytecode:
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
@RequestMapping("/captcha")
public class OpenJobCaptchaController {

    private final ImageCodeGenerator imageCodeGenerator;
    private final SmsCodeGenerator smsCodeGenerator;

    public OpenJobCaptchaController(ImageCodeGenerator imageCodeGenerator, SmsCodeGenerator smsCodeGenerator) {
        this.imageCodeGenerator = imageCodeGenerator;
        this.smsCodeGenerator = smsCodeGenerator;
    }

    @PostMapping("/create/image")
    public Result<OpenJobCaptchaRespDTO> createImageCode(@RequestBody @Valid OpenJobCaptchaRequest request) {
        OpenJobCaptchaRespDTO openJobCaptchaRespDTO = new OpenJobCaptchaRespDTO();
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getDeviceId());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageValidateCode imageValidateCode = imageCodeGenerator.create(captchaGenerateRequest);
            ImageIO.write(imageValidateCode.getImage(), "JPEG", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String base64ImgCode = Base64Utils.encodeToString(bytes);
            openJobCaptchaRespDTO.setImageCode(base64ImgCode);
            openJobCaptchaRespDTO.setSuccess(true);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
        return Result.succeed(openJobCaptchaRespDTO);
    }

    @PostMapping("/create/sms")
    public Result<OpenJobCaptchaRespDTO> createSmsCode(@RequestBody @Valid OpenJobCaptchaRequest request) {
        OpenJobCaptchaRespDTO openJobCaptchaRespDTO = new OpenJobCaptchaRespDTO();
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getDeviceId());
        try {
            ValidateCode validateCode = smsCodeGenerator.create(captchaGenerateRequest);
            openJobCaptchaRespDTO.setSuccess(true);
            log.info("向手机号: {}发送短信验证码: {}", request.getMobile(), validateCode.getCode());
        } catch (ValidateCodeException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
        return Result.succeed(openJobCaptchaRespDTO);
    }
}
```

## 扩展示例

### 1. 替换原有的验证码生成方式

比如要替换掉原来的短信验证码生成方式

```java
@Component
public class ImageCodeGenerator extends AbstractCaptchaGenerator<ImageValidateCode> {

  private final CaptchaProperties captchaProperties;
  private final KaptchaProducer kaptchaProducer;

  public ImageCodeGenerator(CaptchaRepository captchaRepository,
                            CaptchaProperties captchaProperties,
                            KaptchaProducer kaptchaProducer) {
    super(captchaRepository);
    this.captchaProperties = captchaProperties;
    this.kaptchaProducer = kaptchaProducer;
  }

  @Override
  public ImageValidateCode generate() throws ValidateCodeException {
    String text = kaptchaProducer.createText();
    BufferedImage image = kaptchaProducer.createImage(text);
    return new ImageValidateCode(image, text, captchaProperties.getImage().getExpireTime());
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

## 版本更新说明

### 1.0.1

1. 去掉了发送回调接口

2. 对验证码生成及验证整体进行了重构和优化

3. 加入了数学表达式图形验证码功能

4. 配置优化，使用了 @NestedConfigurationProperty，配置时有提示

### 1.0.3 

1. 细化异常分类，可以针对不同的异常做出不同的处理


### 1.0.4

1. 增加本地验证码存储策略
2. 调整验证码存储策略默认为本地存储