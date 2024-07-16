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
package com.openbytecode.starter.limiter.annotation;


import com.openbytecode.starter.limiter.generator.SimpleKeyGenerator;

import java.lang.annotation.*;

/**
 * @author lijunping
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流 key {@link SimpleKeyGenerator}
     */
    String limitKey() default "";

    /**
     * 令牌流入速率，单位秒
     * How many requests per second do you want a user to be allowed to do?
     */
    int replenishRate() default 1;

    /**
     * 桶容量
     * How much bursting do you want to allow?
     */
    int burstCapacity() default 10;

    /**
     * 许可数
     * How many tokens are requested per request?
     */
    int requestedTokens() default 1;



}
