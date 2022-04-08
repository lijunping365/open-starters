package com.saucesubfresh.starter.oauth.properties.social.mini;

import lombok.Data;

/**
 * @author 李俊平
 * @date 2020-06-23 10:44
 */
@Data
public class ByteDanceProperties {

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
  private String providerId = "tt";
}
