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
package com.saucesubfresh.starter.security.exception;

import com.saucesubfresh.starter.security.enums.SecurityExceptionEnum;
import lombok.Data;

/**
 * 认证异常类
 *
 * @author lijunping
 */
@Data
public class SecurityException extends RuntimeException{

    private int code;

    public SecurityException(String msg){
        super(msg);
    }

    public SecurityException(int code) {
        super();
        this.code = code;
    }

    public SecurityException(SecurityExceptionEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public SecurityException(int code, String message) {
        super(message);
        this.code = code;
    }


    /**
     * 禁止访问
     */
    public static final int FORBIDDEN = 403;

    /**
     * 请求头中认证信息不能为空
     */
    public static final int NON_AUTHENTICATION = 402;

    /**
     * 认证信息错误或已失效
     */
    public static final int UNAUTHORIZED = 401;

}
