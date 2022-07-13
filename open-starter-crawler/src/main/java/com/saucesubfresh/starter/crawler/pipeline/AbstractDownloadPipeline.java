package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.exception.CrawlerException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 网页下载抽象类
 */
@Slf4j
public abstract class AbstractDownloadPipeline implements DownloadPipeline {

    @Override
    public void process(SpiderRequest request, SpiderResponse response) {
        boolean success = false;
        int maxTimes = request.getRetryTimes();
        int currentTimes = 0;
        while (!success) {
            try {
                doDownload(request, response);
                success = true;
            }catch (CrawlerException e){
                log.error(e.getMessage(), e);
            }
            if (!success) {
                currentTimes++;
                if (currentTimes > maxTimes) {
                    throw new CrawlerException("The number of download retries reaches the upper limit, " +
                            "the maximum number of times：" + maxTimes);
                }
                sleep(request.getSleepTime());
            }
        }
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Thread interrupted when sleep",e);
        }
    }

    protected abstract void doDownload(SpiderRequest request, SpiderResponse response) throws CrawlerException;
}
