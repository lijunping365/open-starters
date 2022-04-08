package com.saucesubfresh.starter.captcha.core.image;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;
import lombok.Data;
import java.awt.image.BufferedImage;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@Data
public class ImageValidateCode extends ValidateCode {
  /**
   * 图片
   */
  private BufferedImage image;


  public ImageValidateCode(BufferedImage image, String code, int expireIn) {
    super(code, expireIn);
    this.image = image;
  }

}
