package com.saucesubfresh.starter.crawler.handler;


import com.saucesubfresh.starter.crawler.pipeline.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
public class DefaultCrawlerSpiderHandler extends AbstractCrawlerSpiderHandler {

    private final List<Pipeline> pipelines = new ArrayList<>();

    @Override
    protected List<Pipeline> getPipeline() {
        return pipelines;
    }
}
