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
import okhttp3.*;

import java.util.Map;
import java.util.Objects;

/**
 * @author lijunping
 **/
@Slf4j
public class OkHttpExecutor extends AbstractHttpExecutor implements HttpExecutor {

    private final OkHttpClient client;

    public OkHttpExecutor(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public String doGet(HttpRequest httpRequest) throws HttpException {
        StringBuffer sb = getQueryString(httpRequest.getUrl(),httpRequest.getParams());
        Request request = new Request.Builder()
                .url(sb.toString())
                .headers(Headers.of(httpRequest.getHeaders()))
                .build();
        return execNewCall(request);
    }

    @Override
    public String doPost(HttpRequest httpRequest) throws HttpException{
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = FormBody.create(httpRequest.getData(), mediaType);
        Request request = new Request.Builder()
                .url(httpRequest.getUrl())
                .headers(Headers.of(httpRequest.getHeaders()))
                .post(body)
                .build();
        return execNewCall(request);
    }

    private String execNewCall(Request request) throws HttpException{
        try (Response response = client.newCall(request).execute()) {
            return Objects.nonNull(response.body()) && response.isSuccessful() ? response.body().string() : null;
        } catch (Exception e) {
            log.error("okhttp3 execute error >> ex = {}", e.getMessage());
            throw new HttpException(e.getMessage());
        }
    }

    private StringBuffer getQueryString(String url, Map<String,String> queries){
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return sb;
    }
}