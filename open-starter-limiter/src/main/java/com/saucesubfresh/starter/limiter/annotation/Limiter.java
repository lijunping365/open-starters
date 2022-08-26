package com.saucesubfresh.starter.limiter.annotation;

import com.saucesubfresh.starter.limiter.generator.KeyGenerator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

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
     * 桶容量
     */
    int capacity() default 10;

    /**
     * 许可数
     */
    int permits() default 1;

    /**
     * 令牌流入速率
     */
    double rate() default 1;

}
