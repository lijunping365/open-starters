package com.saucesubfresh.starter.crawler.pipeline;


import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;

/**
 * @author lijunping on 2022/4/15
 */
public interface Pipeline {

    void handler(SpiderRequest request, SpiderResponse response);
}
