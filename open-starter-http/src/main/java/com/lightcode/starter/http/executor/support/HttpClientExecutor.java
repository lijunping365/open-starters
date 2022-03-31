package com.lightcode.starter.http.executor.support;

import com.lightcode.starter.http.exception.HttpException;
import com.lightcode.starter.http.executor.AbstractHttpExecutor;
import com.lightcode.starter.http.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientExecutor extends AbstractHttpExecutor implements InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    private CloseableHttpClient client;

    @Override
    public String doGet(HttpRequest request) throws HttpException {
        HttpGet httpGet;
        try {
            URIBuilder uri = new URIBuilder(request.getUrl());
            handlerParams(uri, request.getParams());
            httpGet = new HttpGet(uri.build());
            setHeader(httpGet, request.getHeaders());
        } catch (URISyntaxException e) {
            log.error("HttpClient_build_exception",e);
            throw new HttpException(e.getMessage());
        }
        return execute(httpGet);
    }

    @Override
    public String doPost(HttpRequest request) throws HttpException{
        HttpPost httpPost = new HttpPost(request.getUrl());
        try {
            handlerData(httpPost, request.getParams());
            setHeader(httpPost, request.getHeaders());
        } catch (IOException e) {
            log.error("HttpClient_build_exception",e);
            throw new HttpException(e.getMessage());
        }
        return execute(httpPost);
    }

    private String execute(HttpRequestBase httpRequestBase) throws HttpException{
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpRequestBase);
            return handlerResponse(response);
        } catch (IOException e) {
            log.error("HttpClient_execute_exception",e);
            throw new HttpException(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("HttpClient_CloseableHttpResponse关闭失败", e);
            }
        }
    }

    private void handlerParams(URIBuilder uri, Map<String, String> params){
        if (params != null) {
            for (String key : params.keySet()) {
                uri.addParameter(key, params.get(key));
            }
        }
    }

    private void handlerData(HttpPost httpPost, Map<String, String> data) throws UnsupportedEncodingException {
        if (data != null){
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : data.keySet()) {
                paramList.add(new BasicNameValuePair(key, data.get(key)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Charset.defaultCharset());
            httpPost.setEntity(entity);
        }
    }

    private void setHeader(HttpRequestBase requestBase, Map<String,String> headers){
        if (headers != null){
            for (String key : headers.keySet()) {
                requestBase.setHeader(key,headers.get(key));
            }
        }
    }

    private String handlerResponse(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200){
            return null;
        }
        return EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            this.client = this.beanFactory.getBean(CloseableHttpClient.class);
        } catch (BeansException e) {
            log.warn("No CloseableHttpClient instance is provided");
        }
    }
}