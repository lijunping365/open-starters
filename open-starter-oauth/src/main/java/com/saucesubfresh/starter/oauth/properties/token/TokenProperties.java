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
package com.saucesubfresh.starter.oauth.properties.token;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;

/**
 * @author lijunping
 */
@Data
public class TokenProperties {

    /**
     * accessToken 有效期，单位秒，默认 1 天
     */
    private long accessTokenExpiresIn = 24 * 3600;

    /**
     * redis token key 前缀
     */
    private String tokenPrefix = "access_token:";

    /**
     * jwt 加密密钥 key，注意我们使用的是 SignatureAlgorithm.HS256，所以 secretKeyBytes.length * 8 必须大于 256
     */
    private String secretKey = "ThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKey";

    /**
     * secret key byte array.
     */
    private byte[] secretKeyBytes;

    public byte[] getSecretKeyBytes() {
        if (secretKeyBytes == null && secretKey != null) {
            secretKeyBytes = Decoders.BASE64.decode(secretKey);
        }
        return secretKeyBytes;
    }

}
