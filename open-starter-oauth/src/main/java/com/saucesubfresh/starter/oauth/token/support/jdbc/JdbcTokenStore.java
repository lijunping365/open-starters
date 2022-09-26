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
package com.saucesubfresh.starter.oauth.token.support.jdbc;

import com.saucesubfresh.starter.oauth.authentication.Authentication;
import com.saucesubfresh.starter.oauth.token.AbstractTokenStore;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenEnhancer;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC token store，把 token 及 token 的映射保存到数据库中
 *
 * @author lijunping
 */
@Slf4j
public class JdbcTokenStore extends AbstractTokenStore {

    public JdbcTokenStore(TokenEnhancer tokenEnhancer) {
        super(tokenEnhancer);
    }

    @Override
    public AccessToken doGenerateToken(Authentication authentication) {
        return null;
    }

}
