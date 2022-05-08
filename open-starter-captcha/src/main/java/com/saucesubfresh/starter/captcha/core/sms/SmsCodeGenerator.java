package com.saucesubfresh.starter.captcha.core.sms;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
public class SmsCodeGenerator extends AbstractCaptchaGenerator<ValidateCode> {

    private final CaptchaProperties captchaProperties;

    public SmsCodeGenerator(CaptchaRepository captchaRepository, CaptchaProperties captchaProperties) {
        super(captchaRepository);
        this.captchaProperties = captchaProperties;
    }

    @Override
    public ValidateCode generate() throws ValidateCodeException {
        String code = RandomStringUtils.randomNumeric(captchaProperties.getSms().getLength());
        return new ValidateCode(code, captchaProperties.getSms().getExpireTime());
    }
}
