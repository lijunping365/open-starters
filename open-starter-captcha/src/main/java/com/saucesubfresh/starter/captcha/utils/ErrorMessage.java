package com.saucesubfresh.starter.captcha.utils;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@ToString
public class ErrorMessage {
  /**
   * 错误Bean
   */
  private Class<?> beanClass;
  /**
   * 错误详细信息列表
   */
  private Set<ErrorMessageDetail> detailList;

}
