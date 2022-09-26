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
package com.saucesubfresh.starter.http.executor;


import com.saucesubfresh.starter.http.constants.HttpConstant;
import com.saucesubfresh.starter.http.exception.HttpException;
import com.saucesubfresh.starter.http.request.HttpRequest;

/**
 * @author lijunping
 */
public abstract class AbstractHttpExecutor implements HttpExecutor{

    @Override
    public String execute(HttpRequest httpRequest) throws HttpException {
        HttpConstant.Method method = HttpConstant.Method.valueOf(httpRequest.getMethod());
        switch (method){
            case GET:{
                return doGet(httpRequest);
            }
            case POST:{
                return doPost(httpRequest);
            }
        }
        throw new HttpException("Unsupported method：" + httpRequest.getMethod());
    }

    public abstract String doGet(HttpRequest httpRequest) throws HttpException;

    public abstract String doPost(HttpRequest httpRequest) throws HttpException;
}
