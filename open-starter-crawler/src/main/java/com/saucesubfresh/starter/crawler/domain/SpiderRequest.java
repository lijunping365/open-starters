package com.saucesubfresh.starter.crawler.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 爬虫请求对象，对象包含要抓取的网址和一些附加信息
 */
@Data
public class SpiderRequest implements Serializable {
    private static final long serialVersionUID = 2062192774891352043L;
    /**
     * 任务 id
     */
    private Long taskId;
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
     * 请求参数
     */
    private Map<String, String> params;
    /**
     * 请求头
     */
    private Map<String, String> headers;
    /**
     * http 代理
     */
    private Proxy proxy;
    /**
     * 处理器名称
     */
    private String handlerName;
    /**
     * 数据抽取规则
     */
    private List<FieldExtractor> extract;
    /**
     * 下载重试次数
     */
    private int retryTimes = 0;
    /**
     * 间隔时间
     */
    private int sleepTime = 5000;
}
