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
package com.openbytecode.starter.security.service;


import com.openbytecode.starter.security.domain.Authentication;

/**
 * @author lijunping
 */
public interface TokenService {
    /**
     * 读取认证信息
     * 通过请求头中的 token 判断是不是我们系统颁发的，并判断token有没有过期
     * 如果根据 accessToken 未找到用户信息，或者 accessToken 已过期，就抛出 401
     * @param accessToken
     * @return
     */
    Authentication readAuthentication(String accessToken);
}
