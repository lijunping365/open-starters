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
import com.saucesubfresh.starter.crawler.factory.BeanProxyFactory;
import com.saucesubfresh.starter.crawler.factory.DefaultBeanProxyFactory;
import com.saucesubfresh.starter.crawler.handler.DefaultDownloadHandler;
import com.saucesubfresh.starter.crawler.handler.DefaultResultSetHandler;
import com.saucesubfresh.starter.crawler.handler.DownloadHandler;
import com.saucesubfresh.starter.crawler.handler.ResultSetHandler;
import com.saucesubfresh.starter.crawler.plugin.Interceptor;
import com.saucesubfresh.starter.crawler.plugin.InterceptorChain;
import com.saucesubfresh.starter.crawler.properties.CrawlerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
    public ResultSetHandler resultHandler(){
        return new DefaultResultSetHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public BeanProxyFactory beanProxyFactory(){
        return new DefaultBeanProxyFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public CrawlerExecutor crawlerExecutor(BeanProxyFactory beanProxyFactory){
        return new DefaultCrawlerExecutor(beanProxyFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InterceptorChain interceptorChain(List<Interceptor>interceptors){
        return new InterceptorChain(interceptors);
    }

}
