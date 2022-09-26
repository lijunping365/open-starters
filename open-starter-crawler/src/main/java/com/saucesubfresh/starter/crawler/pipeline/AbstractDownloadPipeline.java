/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.exception.CrawlerException;
import lombok.extern.slf4j.Slf4j;

/**
 * 网页下载抽象类
 *
 * @author lijunping
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
