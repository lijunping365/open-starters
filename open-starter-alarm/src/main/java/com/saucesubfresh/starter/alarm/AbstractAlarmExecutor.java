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
package com.saucesubfresh.starter.alarm;

import com.saucesubfresh.starter.alarm.exception.AlarmException;
import com.saucesubfresh.starter.alarm.request.BaseAlarmRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author lijunping
 */
@Slf4j
public abstract class AbstractAlarmExecutor<T extends BaseAlarmRequest> implements AlarmExecutor<T> {

    private static final int HTTP_CONNECT_TIMEOUT = 1000;
    private static final int HTTP_CONNECTION_REQUEST_TIMEOUT = 1000;
    private static final int HTTP_SOCKET_TIMEOUT = 10000;
    private final RequestConfig requestConfig;

    public AbstractAlarmExecutor() {
        this.requestConfig = RequestConfig.custom()
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(HTTP_CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .build();
    }

    /**
     * Send alarm message to remote endpoint
     */
    protected String sendAlarmMessage(CloseableHttpClient httpClient, String url, String requestBody) throws AlarmException {
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            post.setEntity(entity);
            httpResponse = httpClient.execute(post);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine != null && statusLine.getStatusCode() != HttpStatus.SC_OK) {
                log.error("send alarm to {} failure. Response code: {}", url, statusLine.getStatusCode());
            }
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Throwable e) {
            log.error("send alarm to {} failure.", url, e);
            throw new AlarmException(e.getMessage());
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
