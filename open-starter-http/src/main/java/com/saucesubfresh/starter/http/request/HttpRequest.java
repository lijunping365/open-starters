package com.saucesubfresh.starter.http.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: 李俊平
 * @Date: 2021-07-25 11:14
 */
@Data
public class HttpRequest implements Serializable {
    /**
     * 请求 url
     */
    private String url;
    /**
     * 请求方式，目前支持 Get，Post
     */
    private String method;
    /**
     * Post 请求参数，json 字符串
     */
    private String data;
    /**
     * Get 请求参数
     */
    private Map<String, String> params;
    /**
     * 请求头
     */
    private Map<String, String> headers;
}
