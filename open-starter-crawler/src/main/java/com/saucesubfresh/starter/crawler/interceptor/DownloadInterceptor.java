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
package com.saucesubfresh.starter.crawler.interceptor;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.handler.DownloadHandler;
import com.saucesubfresh.starter.crawler.plugin.*;

/**
 * @author lijunping on 2022/10/27
 */
@Intercepts({@Signature(type = DownloadHandler.class, method = "download", args = {SpiderRequest.class})})
public class DownloadInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("成功拦截了 DownloadHandler 的 download 方法");
        return invocation.proceed();//调用原方法;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);//把被拦截对象生成一个代理对象
    }
}
