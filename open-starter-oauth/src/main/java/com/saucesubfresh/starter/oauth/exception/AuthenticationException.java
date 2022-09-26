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
package com.saucesubfresh.starter.oauth.exception;

import com.saucesubfresh.starter.oauth.enums.OAuthExceptionEnum;
import lombok.Data;

/**
 * 认证异常类
 *
 * @author lijunping
 */
@Data
public class AuthenticationException extends RuntimeException{

    private Integer code;

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(OAuthExceptionEnum OAuthExceptionEnum) {
        super(OAuthExceptionEnum.getMsg());
        this.code = OAuthExceptionEnum.getCode();
    }

    public AuthenticationException(Integer code, String message) {
        super(message);
        this.code = code;
    }


}
