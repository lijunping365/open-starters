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
package com.saucesubfresh.starter.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 根据方法对其返回结果进行缓存，下次请求时，如果缓存存在，则直接读取缓存数据返回；
 * 如果缓存不存在，则执行方法，并把返回的结果存入缓存中。一般用在查询方法上。
 *
 * @author lijunping
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenCacheable {

    /**
     * 缓存名称
     * @return
     */
    @AliasFor("cacheName")
    String value() default "";

    /**
     * 缓存名称
     * @return
     */
    @AliasFor("value")
    String cacheName() default "";

    /**
     * 缓存 key， 如果未指定则会使用 KeyGenerator 的默认实现去生成
     * @return
     */
    String key() default "";
}
