package com.saucesubfresh.starter.captcha.core.image;

import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;

import java.awt.image.BufferedImage;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
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
