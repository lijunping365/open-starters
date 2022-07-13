package com.saucesubfresh.starter.crawler.pipeline;


import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;

/**
 * 流水线处理
 *
 * @author lijunping on 2022/4/15
 */
public interface Pipeline {

    void process(SpiderRequest request, SpiderResponse response);
}
