package com.saucesubfresh.starter.captcha.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * 用于统一管理项目中所有由yml或properties文件传入的变量值
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.captcha")
public class CaptchaProperties {

  /**封装图片验证吗相关的属性*/
  @NestedConfigurationProperty
  private ImageCodeProperties image = new ImageCodeProperties();

  /**封装算数图片验证吗相关的属性*/
  @NestedConfigurationProperty
  private MathImageCodeProperties math = new MathImageCodeProperties();

  /**封装短信验证吗相关的属性*/
  @NestedConfigurationProperty
  private SmsCodeProperties sms = new SmsCodeProperties();

  /***封装扫码登陆相关的配置*/
  @NestedConfigurationProperty
  private ScanCodeProperties scan = new ScanCodeProperties();

}
