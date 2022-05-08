package com.saucesubfresh.starter.captcha.core.math;

import com.saucesubfresh.starter.captcha.core.image.ImageValidateCode;
import com.saucesubfresh.starter.captcha.core.image.kaptcha.KaptchaProducer;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-05-08 16:10
 */
public class MathImageCodeGenerator extends AbstractCaptchaGenerator<ImageValidateCode> {

    private final CaptchaProperties captchaProperties;
    private final KaptchaProducer kaptchaProducer;
    private final MathTextProducer mathTextProducer;

    public MathImageCodeGenerator(CaptchaRepository captchaRepository,
                                  CaptchaProperties captchaProperties,
                                  KaptchaProducer kaptchaProducer,
                                  MathTextProducer mathTextProducer) {
        super(captchaRepository);
        this.captchaProperties = captchaProperties;
        this.kaptchaProducer = kaptchaProducer;
        this.mathTextProducer = mathTextProducer;
    }

    @Override
    protected ImageValidateCode generate() throws ValidateCodeException {
        List<String> list = mathTextProducer.getText();
        String text = list.get(0);
        String sum = list.get(1);
        BufferedImage image = kaptchaProducer.createImage(text);
        return new ImageValidateCode(image, sum, captchaProperties.getImage().getExpireTime());
    }
}
