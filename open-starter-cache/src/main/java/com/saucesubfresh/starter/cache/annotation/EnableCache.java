package com.saucesubfresh.starter.cache.annotation;

import com.saucesubfresh.starter.cache.config.CacheAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lijunping on 2022/5/25
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CacheAutoConfiguration.class)
public @interface EnableCache {

}
