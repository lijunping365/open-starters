package com.saucesubfresh.starter.oauth.properties.token;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;

/**
 * @author lijunping on 2021/12/8
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
