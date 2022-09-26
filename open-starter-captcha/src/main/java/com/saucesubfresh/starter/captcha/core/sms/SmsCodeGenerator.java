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
package com.saucesubfresh.starter.captcha.core.sms;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.AbstractCaptchaGenerator;
import com.saucesubfresh.starter.captcha.properties.CaptchaProperties;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author lijunping
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
