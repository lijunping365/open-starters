package com.lightcode.starter.oauth.properties.token;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;

/**
 * @author lijunping on 2021/12/8
 */
@Data
public class TokenProperties {

    /**
     * accessToken 有效期，单位天，默认 1 天
     */
    private Integer accessTokenExpiresIn = 1;

    /**
     * jwt 加密密钥 key
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
