package com.lightcode.starter.captcha.generator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaptchaGenerator {

    /**
     * filter 名称
     */
    String value();
}
