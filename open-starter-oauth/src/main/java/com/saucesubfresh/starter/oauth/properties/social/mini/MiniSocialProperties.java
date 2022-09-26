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
package com.saucesubfresh.starter.oauth.properties.social.mini;

import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 小程序第三方登录相关配置
 *
 * @author lijunping
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
