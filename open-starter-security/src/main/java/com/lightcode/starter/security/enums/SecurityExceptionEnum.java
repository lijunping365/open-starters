package com.lightcode.starter.security.enums;

/**
 * 结果枚举
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
