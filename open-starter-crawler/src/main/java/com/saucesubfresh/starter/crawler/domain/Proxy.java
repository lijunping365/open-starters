package com.saucesubfresh.starter.crawler.domain;

import lombok.Data;

/**
 * @author lijunping on 2022/4/19
 */
@Data
public class Proxy {

    /**
     * 1: http | 2: https
     */
    private Integer scheme;

    /**
     * ip
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
