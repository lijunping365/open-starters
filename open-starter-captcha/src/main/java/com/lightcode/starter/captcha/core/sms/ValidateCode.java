package com.lightcode.starter.captcha.core.sms;

import lombok.Data;

import java.io.Serializable;

/**
 *  @author : lijunping
 *  @weixin : ilwq18242076871
 * 该类默认就是短信验证码
 * Description: 存放到redis里的对象必须是序列化的，所以这里要实现Serializable接口
 */
@Data
public class ValidateCode implements Serializable {
  private static final long serialVersionUID = -5882129757498488074L;

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
