package com.saucesubfresh.starter.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 根据方法对其返回结果进行缓存，下次请求时，如果缓存存在，则直接读取缓存数据返回；
 * 如果缓存不存在，则执行方法，并把返回的结果存入缓存中。一般用在查询方法上。
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenCachePut {

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
