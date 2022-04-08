package com.saucesubfresh.starter.captcha.core.sms;

import lombok.Data;

import java.io.Serializable;

/**
 *  @author : lijunping
 *  @weixin : ilwq18242076871
 * 该类默认就是短信验证码
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
