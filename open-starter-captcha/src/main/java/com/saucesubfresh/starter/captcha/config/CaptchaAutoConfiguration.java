package com.saucesubfresh.starter.captcha.config;

import com.saucesubfresh.starter.captcha.core.image.ImageCodeGenerator;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.DefaultKaptchaProducer;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.components.*;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.components.impl.*;
import com.saucesubfresh.starter.captcha.core.math.DefaultMathTextCreator;
import com.saucesubfresh.starter.captcha.core.math.MathImageCodeGenerator;
import com.saucesubfresh.starter.captcha.core.math.MathTextProducer;
import com.saucesubfresh.starter.captcha.core.scan.ScanCodeGenerator;
import com.saucesubfresh.starter.captcha.core.sms.SmsCodeGenerator;
import com.saucesubfresh.starter.captcha.processor.CaptchaVerifyProcessor;
import com.saucesubfresh.starter.captcha.processor.DefaultCaptchaVerifyProcessor;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import com.saucesubfresh.starter.captcha.repository.redis.RedisCaptchaRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(StringRedisTemplate.class)
  public CaptchaRepository captchaRepository(StringRedisTemplate stringRedisTemplate){
    return new RedisCaptchaRepository(stringRedisTemplate);
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
  public MathTextProducer mathTextProducer(CaptchaProperties captchaProperties){
    return new DefaultMathTextCreator(captchaProperties);
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
                                                       KaptchaProducer kaptchaProducer,
                                                       MathTextProducer mathTextProducer) {
    return new MathImageCodeGenerator(captchaRepository, properties, kaptchaProducer, mathTextProducer);
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
