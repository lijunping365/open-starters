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
package com.saucesubfresh.starter.captcha.config;

import com.saucesubfresh.starter.captcha.core.image.ImageCodeGenerator;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.DefaultKaptchaProducer;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.components.*;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.components.impl.*;
import com.saucesubfresh.starter.captcha.core.math.MathImageCodeGenerator;
import com.saucesubfresh.starter.captcha.core.scan.ScanCodeGenerator;
import com.saucesubfresh.starter.captcha.core.sms.SmsCodeGenerator;
import com.saucesubfresh.starter.captcha.processor.CaptchaVerifyProcessor;
import com.saucesubfresh.starter.captcha.processor.DefaultCaptchaVerifyProcessor;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import com.saucesubfresh.starter.captcha.repository.LocalCaptchaRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public CaptchaRepository captchaRepository(){
    return new LocalCaptchaRepository();
  }

  @Bean
  @ConditionalOnMissingBean
  public KaptchaProducer producer(CaptchaProperties captchaProperties,
                                  BackgroundProducer backgroundProducer,
                                  WordRenderer wordRenderer,
                                  GimpyEngine gimpyEngine,
                                  TextProducer textProducer){
    return new DefaultKaptchaProducer(captchaProperties, backgroundProducer, wordRenderer, gimpyEngine, textProducer);
  }

  @Bean
  @ConditionalOnMissingBean
  public TextProducer textProducer(CaptchaProperties captchaProperties){
    return new DefaultTextCreator(captchaProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public NoiseProducer noiseProducer(CaptchaProperties captchaProperties){
    return new DefaultNoise(captchaProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public GimpyEngine gimpyEngine(NoiseProducer noiseProducer){
    return new WaterRipple(noiseProducer);
  }

  @Bean
  @ConditionalOnMissingBean
  public BackgroundProducer backgroundProducer(CaptchaProperties captchaProperties){
    return new DefaultBackground(captchaProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public WordRenderer wordRenderer(CaptchaProperties captchaProperties){
    return new DefaultWordRenderer(captchaProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ImageCodeGenerator imageCodeGenerator(CaptchaRepository captchaRepository,
                                               CaptchaProperties properties,
                                               KaptchaProducer kaptchaProducer) {
    return new ImageCodeGenerator(captchaRepository, properties, kaptchaProducer);
  }

  @Bean
  @ConditionalOnMissingBean
  public MathImageCodeGenerator mathImageCodeGenerator(CaptchaRepository captchaRepository,
                                                       CaptchaProperties properties,
                                                       KaptchaProducer kaptchaProducer) {
    return new MathImageCodeGenerator(captchaRepository, properties, kaptchaProducer);
  }

  @Bean
  @ConditionalOnMissingBean
  public ScanCodeGenerator scanCodeGenerator(CaptchaRepository captchaRepository,
                                             CaptchaProperties properties) {
    return new ScanCodeGenerator(captchaRepository, properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public SmsCodeGenerator smsCodeGenerator(CaptchaRepository captchaRepository,
                                           CaptchaProperties properties) {
    return new SmsCodeGenerator(captchaRepository, properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public CaptchaVerifyProcessor captchaVerifyProcessor(CaptchaRepository captchaRepository){
    return new DefaultCaptchaVerifyProcessor(captchaRepository);
  }
}
