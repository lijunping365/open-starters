package com.saucesubfresh.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * Use Cache Function
 * @author lijunping on 2022/5/20
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

}
