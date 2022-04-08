package com.saucesubfresh.starter.http.annotation;

import com.saucesubfresh.starter.http.enums.HttpExecutorType;
import com.saucesubfresh.starter.http.selector.HttpExecutorSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lijunping on 2022/1/20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({HttpExecutorSelector.class})
public @interface EnableHttpExecutor {

    HttpExecutorType clientType() default HttpExecutorType.OKHTTP;
}
