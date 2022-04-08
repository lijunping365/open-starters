package com.saucesubfresh.starter.oauth.properties.social.app;

import lombok.Data;

/**
 * 微信 app
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class WxSocialProperties {

  /**
   * Application id.
   */
  private String appId;

  /**
   * Application secret.
   */
  private String appSecret;

  /**
   * 第三方 id，用来决定发起第三方登录的 url，默认是 mini
   * */
  private String providerId = "wx";

}
