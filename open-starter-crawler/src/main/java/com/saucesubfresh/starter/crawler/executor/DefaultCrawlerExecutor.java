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
package com.saucesubfresh.starter.crawler.executor;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.executor.download.DownloadHandler;
import com.saucesubfresh.starter.crawler.executor.result.ResultHandler;

import java.util.List;

/**
 * @author lijunping
 */
public class DefaultCrawlerExecutor implements CrawlerExecutor{

    private final DownloadHandler downloadHandler;
    private final ResultHandler resultHandler;

    public DefaultCrawlerExecutor(DownloadHandler downloadHandler,
                                  ResultHandler resultHandler) {
        this.downloadHandler = downloadHandler;
        this.resultHandler = resultHandler;
    }

    @Override
    public <T> List<T> handler(SpiderRequest request, Class<T> clazz) {
        String response = downloadHandler.download(request);
        return resultHandler.handler(request, response, clazz);
    }
}
