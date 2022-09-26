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
package com.saucesubfresh.starter.oauth.component;


import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的认证失败处理器
 *
 * @author lijunping
 */
@Slf4j
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailureHandler(AuthenticationException authenticationException) {
        log.error("[登录失败-失败原因]:{}",authenticationException.getMessage());
    }
}
