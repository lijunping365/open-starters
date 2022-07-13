package com.saucesubfresh.starter.crawler.handler;


import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;

/**
 * @author lijunping on 2022/1/19
 */
public interface CrawlerSpiderHandler {

    /**
     * Execute jobHandler
     *
     * @param request params
     * @param response response
     */
    void handle(SpiderRequest request, SpiderResponse response);
}
