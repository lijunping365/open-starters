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
package com.saucesubfresh.starter.security.enums;

/**
 * 认证结果枚举
 *
 * @author lijunping
 */
public enum SecurityExceptionEnum {


  UNAUTHORIZED(401, "accessToken error or accessToken has been invalid"),

  NON_AUTHENTICATION(402, "Illegal request header"),

  FORBIDDEN(403, "permission denied");

  private final Integer code;

  private final String msg;

  SecurityExceptionEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

}
