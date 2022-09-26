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
package com.saucesubfresh.starter.crawler.utils;


import com.saucesubfresh.starter.crawler.annotation.ExtractBy;
import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.Selector;
import com.saucesubfresh.starter.crawler.parser.provider.CssSelector;
import com.saucesubfresh.starter.crawler.parser.provider.JsonPathSelector;
import com.saucesubfresh.starter.crawler.parser.provider.RegexSelector;
import com.saucesubfresh.starter.crawler.parser.provider.XpathSelector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lijunping
 */
public class ExtractorUtils {

    public static List<FieldExtractor> getFieldExtractors(Class<?> clazz) {
        List<FieldExtractor> fieldExtractors = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ExtractBy extractBy = field.getAnnotation(ExtractBy.class);
            if (extractBy == null) {
                continue;
            }

            FieldExtractor fieldExtractor = new FieldExtractor();
            fieldExtractor.setExpressionType(extractBy.type().name());
            fieldExtractor.setDefaultValue(extractBy.defaultValue());
            fieldExtractor.setExpressionValue(extractBy.value());
            fieldExtractor.setFieldName(field.getName());
            fieldExtractor.setMulti(extractBy.multi());
            fieldExtractor.setUnique(extractBy.unique());
            fieldExtractors.add(fieldExtractor);
        }

        return fieldExtractors;
    }

    public static Selector getSelector(ExpressionType type, String value) {
        Selector selector;
        switch (type) {
            case Css:
                selector = new CssSelector(value);
                break;
            case Regex:
                selector = new RegexSelector(value);
                break;
            case JsonPath:
                selector = new JsonPathSelector(value);
                break;
            default:
                selector = new XpathSelector(value);
        }
        return selector;
    }
}
