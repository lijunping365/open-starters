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

/**
 * 401
 *
 * 表示认证失败，表示请求没有被认证或者认证失败
 *
 * 场景有 token失效、token缺失、token伪造，导致服务端无法识别身份
 *
 * @author lijunping
 */
public class InvalidBearerTokenException extends SecurityException{

    public InvalidBearerTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidBearerTokenException(String msg) {
        super(msg);
    }
}
