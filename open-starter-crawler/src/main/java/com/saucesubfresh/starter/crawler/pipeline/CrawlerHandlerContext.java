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

/**
 * @author lijunping
 */
public interface CrawlerHandlerContext {

    /**
     * The unique name of the {@link CrawlerHandlerContext}.The name was used when then {@link CrawlerHandler}
     * was added to the {@link CrawlerPipeline}. This name can also be used to access the registered
     * {@link CrawlerHandler} from the {@link CrawlerPipeline}.
     */
    String name();

    /**
     * The {@link CrawlerHandler} that is bound this {@link CrawlerHandlerContext}.
     */
    CrawlerHandler handler();

    /**
     * A {@link CrawlerHandler} received a message.
     *
     * This will result in having the {@link CrawlerHandler#handler(CrawlerHandlerContext, SpiderRequest, Object)}
     * method  called of the next {@link CrawlerHandler} contained in the {@link CrawlerPipeline}.
     */
    void fireCrawlerHandler(SpiderRequest request, Object msg);
}
