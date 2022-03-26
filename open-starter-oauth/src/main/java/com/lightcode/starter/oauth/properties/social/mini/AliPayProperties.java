package com.lightcode.starter.oauth.properties.social.mini;

import lombok.Data;

/**
 * @author 李俊平
 * @date 2020-06-23 10:50
 */
@Data
public class AliPayProperties {

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
  private String providerId = "alipay";
}
