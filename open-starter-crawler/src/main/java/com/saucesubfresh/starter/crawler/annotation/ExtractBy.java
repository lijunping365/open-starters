/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.crawler.annotation;

import com.saucesubfresh.starter.crawler.enums.ExpressionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用于实体类中的属性
 *
 * @author lijunping
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
     * The default value of field
     *
     * @return default value
     */
    String defaultValue() default "";

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
