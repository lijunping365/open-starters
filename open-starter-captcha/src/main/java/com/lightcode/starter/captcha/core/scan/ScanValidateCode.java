package com.lightcode.starter.captcha.core.scan;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * 二维码
 */
@Data
public class ScanValidateCode extends ValidateCode {
  /**
   * 图片
   */
  private BufferedImage image;

    /**
     * 构造-----expireIn 超时时间（单位秒）
     * @param image
     * @param code
     * @param expireIn
     */
    public ScanValidateCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

}
