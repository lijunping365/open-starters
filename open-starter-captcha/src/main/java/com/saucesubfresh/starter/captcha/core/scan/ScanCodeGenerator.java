package com.saucesubfresh.starter.captcha.core.scan;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.properties.ScanCodeProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.UUID;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public class ScanCodeGenerator extends AbstractCaptchaGenerator<ScanValidateCode> {

  private final CaptchaProperties captchaProperties;

  public ScanCodeGenerator(CaptchaRepository captchaRepository, CaptchaProperties captchaProperties) {
    super(captchaRepository);
    this.captchaProperties = captchaProperties;
  }

  @Override
  public ScanValidateCode generate() throws ValidateCodeException {
    String str = UUID.randomUUID().toString();
    String uuid = str.replaceAll("-", "");
    ScanCodeProperties scanCodeProperties = captchaProperties.getScan();

    Hashtable<EncodeHintType, Object> hintTypes = new Hashtable<>();

    hintTypes.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);//设置编码
    hintTypes.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错级别
    hintTypes.put(EncodeHintType.MARGIN, scanCodeProperties.getMargin());//设置外边距

    BitMatrix bitMatrix;
    try {
      bitMatrix = new MultiFormatWriter().encode(uuid, BarcodeFormat.QR_CODE, scanCodeProperties.getWidth(), scanCodeProperties.getHeight(), hintTypes);
    } catch (WriterException e) {
      throw new ValidateCodeException(e.getMessage());
    }

    BufferedImage bufferedImage = new BufferedImage(scanCodeProperties.getWidth(), scanCodeProperties.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < scanCodeProperties.getWidth(); i++) {
      for (int j = 0; j < scanCodeProperties.getHeight(); j++) {
        bufferedImage.setRGB(i, j, bitMatrix.get(i, j) ? Color.black.getRGB() : Color.WHITE.getRGB());
      }
    }

    return new ScanValidateCode(bufferedImage, uuid, captchaProperties.getImage().getExpireTime());
  }
}
