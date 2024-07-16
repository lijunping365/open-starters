/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.oauth.properties.social.web;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 网页第三方登录相关配置
 *
 * @author lijunping
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
