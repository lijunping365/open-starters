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
package com.openbytecode.starter.captcha.core.sms;

import lombok.Data;

import java.io.Serializable;

/**
 * 该类默认就是短信验证码
 *
 * @author lijunping
 */
@Data
public class ValidateCode{

  /**
   * 随机验证码
   */
  private String code;

  /**
   * 验证码有效时间 单位分钟
   */
  private Integer expireTime;

  public ValidateCode() {
  }

  public ValidateCode(String code, Integer expireIn) {
    this.code = code;
    this.expireTime = expireIn;
  }
}
