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
package com.saucesubfresh.starter.openai.config;

import com.saucesubfresh.starter.openai.annotation.EnableOpenAI;
import com.saucesubfresh.starter.openai.properties.OpenAIProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OpenAIProperties.class)
@ConditionalOnBean(annotation = {EnableOpenAI.class})
public class OpenAIAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionPool connectionPool(OpenAIProperties properties) {
        return new ConnectionPool(properties.getPoolSize(), properties.getKeepAlive(), TimeUnit.MINUTES);
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool, OpenAIProperties properties) {
        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(properties.getTimeOut(), TimeUnit.SECONDS)
                .writeTimeout(properties.getTimeOut(), TimeUnit.SECONDS)
                .readTimeout(properties.getTimeOut(), TimeUnit.SECONDS)
                .build();
    }

}
