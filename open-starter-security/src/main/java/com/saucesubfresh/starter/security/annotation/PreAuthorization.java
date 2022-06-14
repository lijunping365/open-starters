package com.saucesubfresh.starter.security.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 权限控制注解
 *
 * @author: 李俊平
 * @Date: 2020-11-19 12:56
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorization {

    @AliasFor("role")
    String value() default "";
    /**
     * 指定用户拥有某个角色才能访问该接口
     */
    @AliasFor("value")
    String role() default "";
}
