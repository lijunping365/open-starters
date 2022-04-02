package com.lightcode.starter.oauth.enums;

/**
 * 结果枚举
 *
 * @author lijunping
 */
public enum OAuthExceptionEnum {

  UNAUTHORIZED(401, "账号未登录"),

  FORBIDDEN(403, "没有该操作权限"),

  USERNAME_OR_PASSWORD_ERROR(1001, "用户名或密码错误"),

  ACCOUNT_LOCKED(1002, "账户已被锁定"),
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
