package com.lightcode.starter.captcha.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.lightcode.starter.captcha.core.image.ImageCodeGenerator;
import com.lightcode.starter.captcha.core.scan.ScanCodeGenerator;
import com.lightcode.starter.captcha.core.sms.SmsCodeGenerator;
import com.lightcode.starter.captcha.generator.ValidateCodeGenerator;
import com.lightcode.starter.captcha.processor.CaptchaProcessor;
import com.lightcode.starter.captcha.processor.DefaultCaptchaProcessor;
import com.lightcode.starter.captcha.properties.CaptchaProperties;
import com.lightcode.starter.captcha.repository.CaptchaRepository;
import com.lightcode.starter.captcha.repository.redis.RedisCaptchaRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Properties;
import static com.google.code.kaptcha.Constants.*;
import static com.google.code.kaptcha.Constants.KAPTCHA_BACKGROUND_CLR_TO;

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
  public Producer producer(CaptchaProperties captchaProperties) {
    Properties properties = new Properties();
    //图片边框，合法值yes，no，默认值yes
    properties.put(KAPTCHA_BORDER, "no");
    //边框颜色，合法值rgb(and optional alpha)或者 white,black,blue，默认值black
    properties.put(KAPTCHA_BORDER_COLOR, "blue");
    //边框厚度，合法值>0,默认值为1
    properties.put(KAPTCHA_BORDER_THICKNESS, "2");
    //图片宽度，默认值200
    properties.put(KAPTCHA_IMAGE_WIDTH, String.valueOf(captchaProperties.getImage().getWidth()));
    //图片高度，默认值50
    properties.put(KAPTCHA_IMAGE_HEIGHT, String.valueOf(captchaProperties.getImage().getHeight()));
    //图片实现类，默认值com.google.code.kaptcha.impl.DefaultKaptcha
    //properties.put(KAPTCHA_PRODUCER_IMPL, com.google.code.kaptcha.impl.DefaultKaptcha);
    //文本实现类,默认值com.google.code.kaptcha.impl.DefaultTextCreator
    //properties.put(KAPTCHA_TEXTPRODUCER_IMPL, "priv.kerlomz.kaptcha.text.impl.DefaultTextCreator");
    //文本集合，验证码值从此集合中获取,默认值abcde2345678gfynmnpwx
    properties.put(KAPTCHA_TEXTPRODUCER_CHAR_STRING, "1234567890");
    //验证码长度,默认值为5
    properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, String.valueOf(captchaProperties.getImage().getLength()));
    //字体,默认值Arial, Courier(如果使用中文验证码，则必须使用中文的字体，否则出现乱码)
    properties.put(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial");
    //字体大小，默认值为40px
    properties.put(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "40");
    //字体颜色，合法值： r,g,b 或者 white,black,blue，默认值black
    properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
    //文字间隔，默认值为2 -->
    properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "6");
    //干扰实现类，默认值com.google.code.kaptcha.impl.DefaultNoise -->
    properties.put(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.DefaultNoise");
    //干扰 颜色，合法值： r,g,b 或者 white,black,blue，默认值black -->
    properties.put(KAPTCHA_NOISE_COLOR, "black");
    /*图片样式：
    水纹 com.google.code.kaptcha.impl.WaterRipple
    鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy
    阴影 com.google.code.kaptcha.impl.ShadowGimpy, 默认值水纹*/
    properties.put(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.WaterRipple");
    //背景实现类，默认值com.google.code.kaptcha.impl.DefaultBackground -->
    //properties.put(KAPTCHA_BACKGROUND_IMPL, "com.google.code.kaptcha.impl.DefaultBackground");
    //背景颜色渐变，开始颜色，默认值lightGray/192,193,193 -->
    properties.put(KAPTCHA_BACKGROUND_CLR_FROM, "255,255,255");
    //背景颜色渐变， 结束颜色，默认值white -->
    properties.put(KAPTCHA_BACKGROUND_CLR_TO, "white");
    //文字渲染器，默认值priv.kerlomz.kaptcha.text.impl.DefaultWordRenderer -->
    //properties.put(KAPTCHA_WORD_IMPL, "priv.kerlomz.kaptcha.text.impl.DefaultWordRenderer");

    Config config = new Config(properties);
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }

  @Bean
  @ConditionalOnMissingBean
  public CaptchaProcessor captchaProcessor(Map<String, ValidateCodeGenerator> validateCodeGeneratorMap, CaptchaRepository captchaRepository){
    return new DefaultCaptchaProcessor<>(validateCodeGeneratorMap,captchaRepository);
  }

  @Bean
  public ImageCodeGenerator imageCodeGenerator(CaptchaProperties properties, Producer producer) {
    return new ImageCodeGenerator(properties, producer);
  }

  @Bean
  public ScanCodeGenerator scanCodeGenerator(CaptchaProperties properties) {
    return new ScanCodeGenerator(properties);
  }

  @Bean
  public SmsCodeGenerator smsCodeGenerator(CaptchaProperties properties) {
    return new SmsCodeGenerator(properties);
  }

}
