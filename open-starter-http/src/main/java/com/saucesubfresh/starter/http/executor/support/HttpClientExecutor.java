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
package com.saucesubfresh.starter.http.executor.support;

import com.saucesubfresh.starter.http.exception.HttpException;
import com.saucesubfresh.starter.http.executor.AbstractHttpExecutor;
import com.saucesubfresh.starter.http.executor.HttpExecutor;
import com.saucesubfresh.starter.http.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author lijunping
 */
@Slf4j
public class HttpClientExecutor extends AbstractHttpExecutor implements HttpExecutor {

    private final CloseableHttpClient client;

    public HttpClientExecutor(CloseableHttpClient client) {
        this.client = client;
    }

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
            handlerData(httpPost, request.getData());
            setHeader(httpPost, request.getHeaders());
        } catch (IOException e) {
            log.error("HttpClient_build_exception",e);
            throw new HttpException(e.getMessage());
        }
        return execute(httpPost);
    }

    private String execute(HttpRequestBase httpRequestBase) throws HttpException{
        try (CloseableHttpResponse response = client.execute(httpRequestBase)) {
            return handlerResponse(response);
        } catch (IOException e) {
            log.error("HttpClient_execute_exception",e);
            throw new HttpException(e.getMessage());
        }
    }

    private void handlerParams(URIBuilder uri, Map<String, String> params){
        if (params != null) {
            for (String key : params.keySet()) {
                uri.addParameter(key, params.get(key));
            }
        }
    }

    private void handlerData(HttpPost httpPost, String data) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(data)){
            StringEntity entity = new StringEntity(data, ContentType.APPLICATION_JSON);
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
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            return EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
        }
        throw new HttpException(response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());
    }
}