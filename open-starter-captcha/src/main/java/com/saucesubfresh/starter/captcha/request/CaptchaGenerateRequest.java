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
package com.saucesubfresh.starter.captcha.request;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.utils.ValidatorUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 验证码生成请求实体类
 *
 * @author lijunping
 */
@Data
@Accessors(chain = true)
public class CaptchaGenerateRequest implements Serializable {
    private static final long serialVersionUID = -3596061866163459943L;

    @NotBlank(message = "请求唯一唯一 id 不能为空")
    private String requestId;

    /**
     * 在请求前检查自身的约束状况
     *
     * @throws ValidateCodeException .
     */
    public void checkConstraints() throws ValidateCodeException {
        try {
            ValidatorUtils.validate(this);
        } catch (ValidationException e) {
            throw new ValidateCodeException(e.getMessage());
        }
    }
}
