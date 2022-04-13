package com.saucesubfresh.starter.captcha.core.image;

import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.generator.ValidateCodeGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;

import java.awt.image.BufferedImage;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

  private final CaptchaProperties captchaProperties;
  private final KaptchaProducer kaptchaProducer;

  public ImageCodeGenerator(CaptchaProperties captchaProperties, KaptchaProducer kaptchaProducer) {
    this.captchaProperties = captchaProperties;
    this.kaptchaProducer = kaptchaProducer;
  }

  @Override
  public ImageValidateCode generate() throws Exception {
    String text = kaptchaProducer.createText();
    BufferedImage image = kaptchaProducer.createImage(text);
    return new ImageValidateCode(image, text, captchaProperties.getImage().getExpireTime());
  }
}
