# 关于图片验证码的一些说明

首先，本项目只使用了 kaptcha 图片验证码的生成功能，存储、校验等都由本项目自身提供

## 图片验证码是如何存储和校验的

使用 kaptcha 生成图片验证码（BufferedImage）之后，我们只需要把生成图片验证码时所需的字符串存储起来即可，因为我们校验也只需要这些字符串。

如果是生成字符串图片验证码，例如图片验证码中的字符串是 "ABCD"，我们只需要存储 "ABCD" 即可

如果是生成算数图片验证码，例如图片验证码中的字符串是 "1+1"，我们只需要存储 "1+1" 的值即可

校验的话就是对比用户传入的验证码和我们存储的验证码一不一致，如果一致那就通过。

## 图片验证码是如何返回给前端的，前端又是如何展示的？

其实有两种方案：

1. 通过 IO 流的形式将图片写到前端，这种方式不管是前端还是后端处理起来都较为麻烦

2. 可以将 BufferedImage 转为一个 Base64 字符串返回到前端，这种方式后端和前端处理起来都较为简便

后端处理流程：

- 使用 ImageIO 将 BufferedImage 写入到 ByteArrayOutputStream 中

- 使用 Base64Utils 将 byte 数组转为字符串并将字符串返回给前端

具体代码如下

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
}
```

前端处理流程：

- 将图片地址设置为 base64 格式

以下为 React 语言

```js
const [imageUrl, setImageUrl] = useState('');

const onGetImageCaptcha = useCallback(async () => {
    fakeMathImageCaptcha({ deviceId: getDeviceId() })
      .then((result) => {
        if (result && result.success) setImageUrl(`data:image/jpeg;base64,${result.imageCode}`);
      })
      .catch((error) => {
        message.success(`获取验证码失败:${error}`);
      });
  }, []);
```

- 直接在 Image 标签中填入 base64 格式的图片地址便可展示

对应的 tsx 代码如下

```tsx
<ProFormCaptcha
  fieldProps={{
    size: 'large',
    prefix: <LockOutlined className={styles.prefixIcon} />,
  }}
  captchaProps={{
    size: 'large',
  }}
  placeholder={intl.formatMessage({
    id: 'pages.login.captcha.placeholder',
    defaultMessage: '请输入验证码',
  })}
  captchaTextRender={() => {
    return (
      <div style={{ width: '100px', height: '100%' }}>
        <Image preview={false} src={imageUrl} />
      </div>
    );
  }}
  onGetCaptcha={onGetImageCaptcha}
  name="captcha"
  rules={[
    {
      required: true,
      message: (
        <FormattedMessage
          id="pages.login.captcha.required"
          defaultMessage="请输入验证码！"
        />
      ),
    },
  ]}
/>
```