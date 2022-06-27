package com.saucesubfresh.starter.oauth.properties.social.mini;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 小程序第三方登录相关配置
 *
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class MiniSocialProperties {

  /**
   * 微信小程序登录相关配置
   */
  @NestedConfigurationProperty
  private WeAppProperties weapp = new WeAppProperties();

  /**
   * 字节跳动小程序登录相关配置
   */
  @NestedConfigurationProperty
  private ByteDanceProperties tt = new ByteDanceProperties();

  /**
   * 支付宝小程序登录相关配置
   */
  @NestedConfigurationProperty
  private AliPayProperties alipay = new AliPayProperties();
}
