package com.saucesubfresh.starter.lottery.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Filter {

    /**
     * filter 名称
     */
    String value();

    /**
     * filter 执行顺序，值越小，越先执行
     */
    int order() default 0;
}
