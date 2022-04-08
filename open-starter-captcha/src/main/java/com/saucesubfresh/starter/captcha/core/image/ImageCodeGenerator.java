package com.saucesubfresh.starter.captcha.core.image;

import com.google.code.kaptcha.Producer;
import com.saucesubfresh.starter.captcha.generator.ValidateCodeGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;

import java.awt.image.BufferedImage;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

  private final CaptchaProperties captchaProperties;
  private final Producer producer;

  public ImageCodeGenerator(CaptchaProperties captchaProperties, Producer producer) {
    this.captchaProperties = captchaProperties;
    this.producer = producer;
  }

  @Override
  public ImageValidateCode generate() throws Exception {
    String text = producer.createText();
    BufferedImage image = producer.createImage(text);
    return new ImageValidateCode(image, text, captchaProperties.getImage().getExpireTime());
  }
}
