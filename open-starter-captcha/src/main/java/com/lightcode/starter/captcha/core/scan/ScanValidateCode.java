package com.lightcode.starter.captcha.core.scan;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@Data
public class ScanValidateCode extends ValidateCode {
  /**
   * 图片
   */
  private BufferedImage image;


  public ScanValidateCode(BufferedImage image, String code, int expireIn) {
      super(code, expireIn);
      this.image = image;
  }

}
