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
package com.openbytecode.starter.crawler.annotation;

import java.lang.annotation.*;

/**
 * 用于实体类
 *
 * @author lijunping
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenCrawler {

    /**
     * 请求 url
     */
    String url();
    /**
     * 爬虫 id
     */
    long spiderId() default 0L;
    /**
     * 请求方式 GET|POST
     */
    String method() default "GET";
    /**
     * 下载重试次数
     */
    int retryTimes() default 0;
    /**
     * 间隔时间
     */
    int sleepTime() default 5000;
    /**
     * 定义提取器是否返回多个结果
     */
    boolean multi() default false;
}
