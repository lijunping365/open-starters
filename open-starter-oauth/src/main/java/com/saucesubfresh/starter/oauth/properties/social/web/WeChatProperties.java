package com.saucesubfresh.starter.oauth.properties.social.web;


import lombok.Data;

/**
 * 微信网页登录
 *
 * @author Administrator
 * @create 2020-03-24-8:09
 **/
@Data
public class WeChatProperties {
  /**
   * Application id.
   */
  private String appId;

  /**
   * Application secret.
   */
  private String appSecret;

  /**
   * 第三方 id，用来决定发起第三方登录的 url，默认是 weChat
   */
  private String providerId = "wechat/callback";

  /**
   * 扫码成功重定向地址
   */
  private String redirectUrl;

  /**
   * 用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即可
   */
  private String scope;


}
