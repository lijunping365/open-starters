package com.saucesubfresh.starter.crawler.handler;


import com.saucesubfresh.starter.crawler.context.CrawlerContext;

/**
 * @author lijunping on 2022/1/19
 */
public interface CrawlerSpiderHandler {

    /**
     * 执行爬虫
     *
     * @param crawlerContext 爬虫上下文
     */
    void handle(CrawlerContext crawlerContext);
}
