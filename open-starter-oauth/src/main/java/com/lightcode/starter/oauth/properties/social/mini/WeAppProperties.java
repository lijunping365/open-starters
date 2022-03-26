package com.lightcode.starter.oauth.properties.social.mini;

import lombok.Data;

/**
 * 微信小程序
 * @author lJwq
 * @date 2020/6/4 20:12
 */
@Data
public class WeAppProperties {

    /**
     * Application id.
     */
    private String appId;

    /**
     * Application secret.
     */
    private String appSecret;

    /**
     * 第三方 id，用来决定发起第三方登录的 url，默认是 mini
     * */
    private String providerId = "weapp";
}
