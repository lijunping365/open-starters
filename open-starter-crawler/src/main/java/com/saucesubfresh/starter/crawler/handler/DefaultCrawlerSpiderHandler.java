package com.saucesubfresh.starter.crawler.handler;


import com.saucesubfresh.starter.crawler.context.CrawlerContext;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.pipeline.Pipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
public class DefaultCrawlerSpiderHandler implements CrawlerSpiderHandler {

    @Override
    public void handle(CrawlerContext context) {
        List<Pipeline> pipelines = context.getPipelines();
        SpiderRequest request = context.getRequest();
        SpiderResponse response = context.getResponse();
        if (CollectionUtils.isEmpty(pipelines)){
            return;
        }
        pipelines.forEach(pipeline -> pipeline.process(request, response));
    }
}
