package com.saucesubfresh.starter.oauth.enums;

/**
 * 结果枚举
 *
 * @author lijunping
 */
public enum OAuthExceptionEnum {

  UNAUTHORIZED(401, "Unauthorized"),

  FORBIDDEN(403, "permission denied"),

  USERNAME_OR_PASSWORD_ERROR(1001, "username or password error"),

  ACCOUNT_LOCKED(1002, "account locked"),
  ;

  private final Integer code;

  private final String msg;

  OAuthExceptionEnum(Integer code, String msg) {
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
