package com.lightcode.starter.captcha.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * 用于统一管理项目中所有由yml或properties文件传入的变量值
 */
@Data
@ConfigurationProperties(prefix = "com.lightcode.captcha")
public class CaptchaProperties {

  /***验证码相关的属性---可能包含图形验证码，短信验证码等，所以对其进行了又一次封装*/
  private ValidateCodeProperties code = new ValidateCodeProperties();

}
