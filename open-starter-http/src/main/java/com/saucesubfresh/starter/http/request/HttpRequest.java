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
    private String url;
    private String method;
    private Map<String, String> params;
    private Map<String, String> headers;
}
