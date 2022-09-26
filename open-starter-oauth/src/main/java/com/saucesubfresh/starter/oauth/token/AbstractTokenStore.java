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
package com.saucesubfresh.starter.oauth.token;

import com.saucesubfresh.starter.oauth.authentication.Authentication;

/**
 * @author lijunping
 */
public abstract class AbstractTokenStore implements TokenStore{

    private final TokenEnhancer tokenEnhancer;

    public AbstractTokenStore(TokenEnhancer tokenEnhancer) {
        this.tokenEnhancer = tokenEnhancer;
    }

    @Override
    public AccessToken generateToken(Authentication authentication) {
        AccessToken result = doGenerateToken(authentication);
        result = tokenEnhancer.enhance(result, authentication);
        return result;
    }

    public abstract AccessToken doGenerateToken(Authentication authentication);
}
