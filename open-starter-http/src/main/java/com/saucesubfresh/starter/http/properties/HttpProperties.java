package com.saucesubfresh.starter.http.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 李俊平
 * @Date: 2021-07-23 09:56
 */
@Data
@ConfigurationProperties(prefix = "com.pro.http")
public class HttpProperties {

    /**
     * 连接池数量
     */
    private Integer poolSize = 200;

    /**
     * 某一个服务每次能并行接收的请求数量。
     */
    private Integer maxPerRoute = 500;

    /**
     * 连接超时时间，单位毫秒
     */
    private Integer timeOut = 5000;

    /**
     * 存活时长，单位 min
     */
    private long keepAlive = 5;
}
