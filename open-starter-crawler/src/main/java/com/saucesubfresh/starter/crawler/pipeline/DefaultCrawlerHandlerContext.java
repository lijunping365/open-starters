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
public class DefaultCrawlerHandlerContext implements CrawlerHandlerContext{

    volatile DefaultCrawlerHandlerContext next;
    volatile DefaultCrawlerHandlerContext prev;
    private CrawlerHandler handler;
    private final String name;

    public DefaultCrawlerHandlerContext(String name) {
        this.name = name;
    }

    public DefaultCrawlerHandlerContext(String name, CrawlerHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public CrawlerHandler handler() {
        return this.handler;
    }

    @Override
    public void fireCrawlerHandler(SpiderRequest request, Object msg) {
        invokeCrawlerHandler(next, request, msg);
    }

    public static void invokeCrawlerHandler(final DefaultCrawlerHandlerContext next, SpiderRequest request, Object msg) {
        if (null != next){
            next.invokeCrawlerHandler(request, msg);
        }
    }

    private void invokeCrawlerHandler(SpiderRequest request, Object msg) {
        if (null != handler){
            handler.handler(this, request, msg);
        } else {
            fireCrawlerHandler(request, msg);
        }
    }
}
