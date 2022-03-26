package com.lightcode.starter.captcha.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties {

  /**
   * 宽度
   */
  private int width = 67;

  /**
   * 高度
   */
  private int height = 23;

  /**
   * 图片格式
   */
  private String imageFormat = "JPEG";

}
