/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.captcha.core.scan;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.openbytecode.starter.captcha.exception.CaptchaGenerationException;
import com.openbytecode.starter.captcha.exception.ValidateCodeException;
import com.openbytecode.starter.captcha.processor.AbstractCaptchaGenerator;
import com.openbytecode.starter.captcha.properties.CaptchaProperties;
import com.openbytecode.starter.captcha.properties.ScanCodeProperties;
import com.openbytecode.starter.captcha.repository.CaptchaRepository;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.UUID;

/**
 * @author lijunping
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
      throw new CaptchaGenerationException(e.getMessage());
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
