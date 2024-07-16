package com.openbytecode.starter.crawler.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpiderResponse implements Serializable {

    private static final long serialVersionUID = -6797781180624813305L;

    /**
     * 原始内容
     */
    private String body;

    /**
     * 采集数据
     */
    private Object data;

}
