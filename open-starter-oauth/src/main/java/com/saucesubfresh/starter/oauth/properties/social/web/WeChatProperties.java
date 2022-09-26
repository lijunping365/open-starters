/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.oauth.properties.social.web;


import lombok.Data;

/**
 * 微信网页登录
 *
 * @author lijunping
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
