package com.saucesubfresh.starter.captcha.core.math;

import com.saucesubfresh.starter.captcha.core.image.ImageValidateCode;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import org.apache.commons.lang3.RandomUtils;

import java.awt.image.BufferedImage;

/**
 * @author: 李俊平
 * @Date: 2022-05-08 16:10
 */
public class MathImageCodeGenerator extends AbstractCaptchaGenerator<ImageValidateCode> {

    private final CaptchaProperties captchaProperties;
    private final KaptchaProducer kaptchaProducer;

    public MathImageCodeGenerator(CaptchaRepository captchaRepository,
                                  CaptchaProperties captchaProperties,
                                  KaptchaProducer kaptchaProducer) {
        super(captchaRepository);
        this.captchaProperties = captchaProperties;
        this.kaptchaProducer = kaptchaProducer;
    }

    @Override
    protected ImageValidateCode generate() throws ValidateCodeException {
        Integer firstNum = RandomUtils.nextInt() % 10 + 1;
        Integer secondNum = RandomUtils.nextInt() % 10 + 1;
        Integer validateCode = firstNum + secondNum;
        BufferedImage image = kaptchaProducer.createImage(firstNum + "+" + secondNum + "=?");
        return new ImageValidateCode(image, String.valueOf(validateCode), captchaProperties.getImage().getExpireTime());
    }
}
