package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.sun.deploy.net.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: httpClient 下载器
 */
@Slf4j
@Component
@AllArgsConstructor
public class DefaultDownloadPipeline extends AbstractDownloadPipeline {

    private final HttpExecutor httpExecutor;

    @Override
    protected void doDownload(SpiderRequest request, SpiderResponse response) throws SpiderException {
        final HttpRequest httpRequest = buildHttpRequest(request);
        try {
            String result = httpExecutor.execute(httpRequest);
            response.setBody(result);
        } catch (HttpException e) {
            log.error("Download error {}", e.getMessage());
            throw new SpiderException(e.getMessage());
        }
    }

}
