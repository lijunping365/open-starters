package com.lightcode.starter.captcha.core.image;

import com.google.code.kaptcha.Producer;
import com.lightcode.starter.captcha.generator.CaptchaGenerator;
import com.lightcode.starter.captcha.generator.ValidateCodeGenerator;
import com.lightcode.starter.captcha.properties.CaptchaProperties;

import java.awt.image.BufferedImage;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:
 * (1)配置文件中可以配置高度、宽度、验证码的位数和过期时间
 */
@CaptchaGenerator("image")
public class ImageCodeGenerator implements ValidateCodeGenerator<ImageValidateCode> {

  private final CaptchaProperties captchaProperties;
  private final Producer producer;

  public ImageCodeGenerator(CaptchaProperties captchaProperties, Producer producer) {
    this.captchaProperties = captchaProperties;
    this.producer = producer;
  }

  @Override
  public ImageValidateCode generate() {
    String text = producer.createText();
    BufferedImage image = producer.createImage(text);
    return new ImageValidateCode(image, text, captchaProperties.getCode().getImage().getExpireTime());
  }
}
