package com.saucesubfresh.starter.oauth.properties.social.app;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * app 第三方登录相关配置
 *
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class AppSocialProperties {

  /**
   * 微信 app 登录相关配置
   */
  @NestedConfigurationProperty
  private WxSocialProperties wx = new WxSocialProperties();
}
