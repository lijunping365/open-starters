package com.saucesubfresh.starter.crawler.context;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.pipeline.Pipeline;
import lombok.Data;

import java.util.List;

/**
 * @author lijunping on 2022/7/26
 */
@Data
public class CrawlerContext {

    private SpiderRequest request;

    private SpiderResponse response;

    private List<Pipeline> pipelines;

}
