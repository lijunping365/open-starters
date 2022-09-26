/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.captcha.core.image;

import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;

import java.awt.image.BufferedImage;

/**
 * @author lijunping
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
