package com.saucesubfresh.starter.oauth.properties.social.web;


import lombok.Data;

/**
 * 配置我们的 appId， appSecret
 * @author Administrator
 * @create 2020-03-23-18:17
 **/
@Data
public class QQProperties {
    /**
     * Application id.
     */
    private String appId;

    /**
     * Application secret.
     */
    private String appSecret;

    // 服务提供商的标示
    private String providerId = "qq/callback";

}
