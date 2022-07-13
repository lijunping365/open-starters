package com.saucesubfresh.starter.crawler.handler;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.pipeline.Pipeline;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lijunping on 2022/4/15
 */
public abstract class AbstractCrawlerSpiderHandler implements CrawlerSpiderHandler {

    @Override
    public void handle(SpiderRequest request, SpiderResponse response) {
        final List<Pipeline> pipelines = this.getPipeline();
        if (CollectionUtils.isEmpty(pipelines)){
            return;
        }
        pipelines.forEach(pipeline -> pipeline.process(request, response));
    }

    protected abstract List<Pipeline> getPipeline();
}
