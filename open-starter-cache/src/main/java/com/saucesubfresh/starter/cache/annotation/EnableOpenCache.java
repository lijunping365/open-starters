package com.saucesubfresh.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * 代表开启缓存
 * @author lijunping on 2022/5/25
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableOpenCache {

}
