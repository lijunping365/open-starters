/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.crawler.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 爬虫请求对象，对象包含要抓取的网址和一些附加信息
 *
 * @author lijunping
 */
@Data
public class SpiderRequest implements Serializable {
    private static final long serialVersionUID = 2062192774891352043L;
    /**
     * 爬虫 id
     */
    private Long spiderId;
    /**
     * 请求 url
     */
    private String url;
    /**
     * 请求方式 GET|POST
     */
    private String method;
    /**
     * 下载重试次数
     */
    private int retryTimes = 0;
    /**
     * 间隔时间
     */
    private int sleepTime = 5000;
    /**
     * 定义提取器是否返回多个结果
     */
    private boolean multi = false;
    /**
     * 请求参数
     */
    private Map<String, String> params;
    /**
     * 请求头
     */
    private Map<String, String> headers;
    /**
     * 数据抽取规则
     */
    private List<FieldExtractor> extract;


}
