package com.saucesubfresh.starter.limiter.annotation;

import com.saucesubfresh.starter.limiter.generator.KeyGenerator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流 key 的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 限流 key，支持 Spel 表达式，生成器 {@link KeyGenerator}
     * @return
     */
    String key() default "";

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