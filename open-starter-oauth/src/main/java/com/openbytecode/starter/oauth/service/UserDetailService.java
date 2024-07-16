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
package com.openbytecode.starter.oauth.service;


import com.openbytecode.starter.oauth.domain.UserDetails;

/**
 * 用户信息接口
 *
 * @author lijunping
 */
public interface UserDetailService {

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 通过手机号获取用户信息
     * @param mobile 手机号
     * @return
     */
    UserDetails loadUserByMobile(String mobile);
}
