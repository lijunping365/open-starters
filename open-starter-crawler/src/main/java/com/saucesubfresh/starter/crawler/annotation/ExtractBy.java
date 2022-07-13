package com.saucesubfresh.starter.crawler.annotation;

import com.saucesubfresh.starter.crawler.enums.ExpressionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * xxxx
 */

@Documented
@Target(ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ExtractBy {

    /**
     * Extractor expression, support XPath, CSS Selector and regex.
     *
     * @return extractor expression
     */
    String value();

    /**
     * Extractor type, support XPath, CSS Selector and regex.
     *
     * @return extractor type
     */
    ExpressionType type() default ExpressionType.XPath;

    /**
     * Define whether the extractor return more than one result.
     * When set to 'true', the extractor return a list of string (so you should define the field as List). <br>
     *
     * @return whether the extractor return more than one result
     */
    boolean multi() default false;

    /**
     * Define Whether this field is involved in unique ID generation.
     * When set to 'true', this field will participate in the unique ID generation. <br>
     *
     * @return Whether this field is involved in unique ID generation
     */
    boolean unique() default false;

}
