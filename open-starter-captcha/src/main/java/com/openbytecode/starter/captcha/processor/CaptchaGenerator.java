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
package com.openbytecode.starter.captcha.processor;

import com.openbytecode.starter.captcha.core.sms.ValidateCode;
import com.openbytecode.starter.captcha.exception.ValidateCodeException;
import com.openbytecode.starter.captcha.request.CaptchaGenerateRequest;

/**
 * 校验码处理器，封装不同校验码的处理逻辑
 *
 * @author lijunping
 */
public interface CaptchaGenerator<T extends ValidateCode> {

    /**
     * 创建校验码
     */
    T create(CaptchaGenerateRequest request) throws ValidateCodeException;
}
