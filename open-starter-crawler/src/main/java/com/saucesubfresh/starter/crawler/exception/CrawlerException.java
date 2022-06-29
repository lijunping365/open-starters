package com.saucesubfresh.starter.crawler.exception;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 爬虫爬取异常类
 */
public class CrawlerException extends RuntimeException{

    public CrawlerException(String message) {
        super(message);
    }
}
