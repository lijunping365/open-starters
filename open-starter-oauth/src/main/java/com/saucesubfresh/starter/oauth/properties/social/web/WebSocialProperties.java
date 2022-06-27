package com.saucesubfresh.starter.oauth.properties.social.web;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 网页第三方登录相关配置
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class WebSocialProperties {

  /**
   * QQ 网页登录相关配置
   */
  @NestedConfigurationProperty
  private QQProperties qq = new QQProperties();

  /**
   * 微信网页登录相关配置
   */
  @NestedConfigurationProperty
  private WeChatProperties weChat = new WeChatProperties();

  /**
   * 支付宝网页登录相关配置
   */
  @NestedConfigurationProperty
  private AliPayProperties alipay = new AliPayProperties();
}
