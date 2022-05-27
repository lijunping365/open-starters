package com.saucesubfresh.starter.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 添加缓存注解
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * 缓存名称
     * @return
     */
    @AliasFor("cacheName")
    String value();

    /**
     * 缓存名称
     * @return
     */
    @AliasFor("value")
    String cacheName();
}
