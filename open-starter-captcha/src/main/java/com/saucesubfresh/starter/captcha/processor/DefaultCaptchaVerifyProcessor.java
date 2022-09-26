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
package com.saucesubfresh.starter.captcha.processor;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import com.saucesubfresh.starter.captcha.request.CaptchaVerifyRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping
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
