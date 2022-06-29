package com.saucesubfresh.starter.http.config;

import com.saucesubfresh.starter.http.constants.HttpConstant;
import com.saucesubfresh.starter.http.executor.HttpExecutor;
import com.saucesubfresh.starter.http.executor.support.HttpClientExecutor;
import com.saucesubfresh.starter.http.executor.support.OkHttpExecutor;
import com.saucesubfresh.starter.http.properties.HttpProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-01-26 09:10
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HttpProperties.class)
public class HttpAutoConfiguration {

    @Bean
    @ConditionalOnBean(OkHttpExecutor.class)
    @ConditionalOnMissingBean
    public ConnectionPool connectionPool(HttpProperties properties) {
        return new ConnectionPool(properties.getPoolSize(), properties.getKeepAlive(), TimeUnit.MINUTES);
    }

    @Bean
    @ConditionalOnBean(OkHttpExecutor.class)
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
    public HttpExecutor okHttpExecutor(OkHttpClient okHttpClient){
        return new OkHttpExecutor(okHttpClient);
    }

    @Bean
    @ConditionalOnBean(HttpClientExecutor.class)
    @ConditionalOnMissingBean
    public PoolingHttpClientConnectionManager connectionManager(HttpProperties properties){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(properties.getMaxPerRoute());
        connectionManager.setMaxTotal(properties.getPoolSize());
        return connectionManager;
    }

    @Bean
    @ConditionalOnBean(HttpClientExecutor.class)
    @ConditionalOnMissingBean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager connectionManager,
                                          HttpProperties properties){
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
}
