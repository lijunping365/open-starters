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
package com.saucesubfresh.starter.crawler.config;

import com.saucesubfresh.starter.crawler.executor.CrawlerExecutor;
import com.saucesubfresh.starter.crawler.executor.DefaultCrawlerExecutor;
import com.saucesubfresh.starter.crawler.executor.download.DefaultDownloadHandler;
import com.saucesubfresh.starter.crawler.executor.download.DownloadHandler;
import com.saucesubfresh.starter.crawler.executor.result.DefaultResultHandler;
import com.saucesubfresh.starter.crawler.executor.result.ResultHandler;
import com.saucesubfresh.starter.crawler.properties.CrawlerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CrawlerProperties.class)
public class CrawlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DownloadHandler downloadPipeline(){
        return new DefaultDownloadHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResultHandler resultHandler(){
        return new DefaultResultHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CrawlerExecutor crawlerExecutor(DownloadHandler downloadPipeline, ResultHandler resultHandler){
        return new DefaultCrawlerExecutor(downloadPipeline, resultHandler);
    }

}
