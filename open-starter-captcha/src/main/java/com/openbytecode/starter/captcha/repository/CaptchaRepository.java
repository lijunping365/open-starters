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
package com.openbytecode.starter.captcha.repository;

import com.openbytecode.starter.captcha.core.sms.ValidateCode;

/**
 * 保存、获取、移除验证码的模版接口
 *
 * @author lijunping
 */
public interface CaptchaRepository {
    /**
     * 保存验证码
     * @param requestId
     * @param code
     */
    <C extends ValidateCode> void save(String requestId, C code);
    /**
     * 获取验证码
     * @param requestId
     * @return
     */
    String get(String requestId);
}
