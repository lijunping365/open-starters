package com.saucesubfresh.starter.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvict {

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
}
