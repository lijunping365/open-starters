package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.exception.CrawlerException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: httpClient 下载器
 */
@Slf4j
public class DefaultDownloadPipeline extends AbstractDownloadPipeline {

    @Override
    protected void doDownload(SpiderRequest request, SpiderResponse response) throws CrawlerException {

    }

}
