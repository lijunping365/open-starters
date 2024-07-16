/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.http.config;

import com.openbytecode.starter.http.constants.HttpConstant;
import com.openbytecode.starter.http.executor.HttpExecutor;
import com.openbytecode.starter.http.executor.support.OkHttpExecutor;
import com.openbytecode.starter.http.properties.HttpProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author lijunping
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionPool connectionPool(HttpProperties properties) {
        return new ConnectionPool(properties.getPoolSize(), properties.getKeepAlive(), TimeUnit.MINUTES);
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool, HttpProperties properties) {
        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(properties.getTimeOut(), TimeUnit.SECONDS)
                .writeTimeout(properties.getTimeOut(), TimeUnit.SECONDS)
                .readTimeout(properties.getTimeOut(), TimeUnit.SECONDS)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public PoolingHttpClientConnectionManager connectionManager(HttpProperties properties){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(properties.getMaxPerRoute());
        connectionManager.setMaxTotal(properties.getPoolSize());
        return connectionManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public CloseableHttpClient httpClient(HttpProperties properties,
                                          PoolingHttpClientConnectionManager connectionManager){
        RequestConfig requestConfig = RequestConfig.custom()
                //指客户端和服务器建立连接后，客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
                .setSocketTimeout(properties.getTimeOut())
                //指从连接池获取连接的 timeout
                .setConnectionRequestTimeout(properties.getTimeOut())
                //指客户端和服务器建立连接的timeout，就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。超时后会ConnectionTimeOutException
                .setConnectTimeout(properties.getTimeOut())
                .build();
        return HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(HttpConstant.UserAgent.USER_AGENT_CHROME)
                .disableAutomaticRetries()
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpExecutor httpExecutor(OkHttpClient okHttpClient){
        return new OkHttpExecutor(okHttpClient);
    }
}
