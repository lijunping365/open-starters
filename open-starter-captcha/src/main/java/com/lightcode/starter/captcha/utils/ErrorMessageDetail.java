package com.lightcode.starter.captcha.utils;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@ToString
public class ErrorMessageDetail {
  /**
   * 错误字段
   */
  private String propertyPath;
  /**
   * 错误信息
   */
  private String message;
  /**
   * 错误值
   */
  private Object invalidValue;
}
