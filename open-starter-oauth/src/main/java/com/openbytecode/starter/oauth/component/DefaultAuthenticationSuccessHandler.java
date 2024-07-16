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
package com.openbytecode.starter.oauth.component;

import com.openbytecode.starter.oauth.authentication.Authentication;
import com.openbytecode.starter.oauth.request.BaseLoginRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的认证成功处理器
 *
 * @author lijunping
 */
@Slf4j
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public <T extends BaseLoginRequest> void onAuthenticationSuccess(Authentication authentication, T request) {
        log.info("[登录成功]-[用户编号：{}]", authentication.getUserDetails().getId());
    }
}
