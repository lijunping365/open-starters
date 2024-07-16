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
package com.openbytecode.starter.captcha.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


/**
 * 用于统一管理项目中所有由yml或properties文件传入的变量值
 *
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.openbytecode.captcha")
public class CaptchaProperties {

  /**封装图片验证吗相关的属性*/
  @NestedConfigurationProperty
  private ImageCodeProperties image = new ImageCodeProperties();

  /**封装算数图片验证吗相关的属性*/
  @NestedConfigurationProperty
  private MathImageCodeProperties math = new MathImageCodeProperties();

  /**封装短信验证吗相关的属性*/
  @NestedConfigurationProperty
  private SmsCodeProperties sms = new SmsCodeProperties();

  /***封装扫码登陆相关的配置*/
  @NestedConfigurationProperty
  private ScanCodeProperties scan = new ScanCodeProperties();

}
