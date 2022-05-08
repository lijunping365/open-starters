package com.saucesubfresh.starter.captcha.processor;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import com.saucesubfresh.starter.captcha.request.CaptchaVerifyRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: 李俊平
 * @Date: 2022-05-08 15:20
 */
public class DefaultCaptchaVerifyProcessor implements CaptchaVerifyProcessor{

    private final CaptchaRepository captchaRepository;

    public DefaultCaptchaVerifyProcessor(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public void validate(CaptchaVerifyRequest request) throws ValidateCodeException{
        request.checkConstraints();
        String validateCode = captchaRepository.get(request.getRequestId());
        String codeInRequest = request.getCode();

        if (StringUtils.isBlank(validateCode)) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(validateCode, codeInRequest)) {
            throw new ValidateCodeException("验证码输入错误");
        }
    }
}
