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
package com.saucesubfresh.starter.captcha.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lijunping
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties {

  /**
   * 图片宽度
   */
  private int width = 67;

  /**
   * 图片高度
   */
  private int height = 23;

  /**
   * 图片格式
   */
  private String imageFormat = "JPEG";

  /**
   * 边框，默认无
   */
  private boolean border = false;

  /**
   * 边框颜色，合法值rgb(and optional alpha)或者 white,black,blue，默认值 black
   */
  private String borderColor = "blue";

  /**
   * 边框厚度，合法值>0,默认值为 2
   */
  private int borderThickness = 2;

  /**
   * 文本集合，验证码值从此集合中获取,默认值 1234567890
   */
  private String resourceText = "1234567890";

  /**
   * 字体,默认值Arial, Courier(如果使用中文验证码，则必须使用中文的字体，否则出现乱码)
   */
  private String fontNames = "Arial";

  /**
   * 字体大小，默认值为40px
   */
  private int fontSize = 40;

  /**
   * 字体颜色，合法值： r,g,b 或者 white,black,blue，默认值black
   */
  private String fontColor = "black";

  /**
   * 文字间隔，默认值为 6
   */
  private int charSpace = 6;

  /**
   * 干扰 颜色，合法值： r,g,b 或者 white,black,blue，默认值black
   */
  private String noiseColor = "black";

  /**
   * 背景颜色渐变，开始颜色，默认值lightGray/192,193,193
   */
  private String backgroundClearFrom = "255,255,255";

  /**
   * 背景颜色渐变， 结束颜色，默认值white
   */
  private String backgroundClearTo = "white";



}
