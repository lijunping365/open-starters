package com.saucesubfresh.starter.crawler.utils;


import com.saucesubfresh.starter.crawler.annotation.ExtractBy;
import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.provider.CssSelector;
import com.saucesubfresh.starter.crawler.parser.provider.JsonPathSelector;
import com.saucesubfresh.starter.crawler.parser.provider.RegexSelector;
import com.saucesubfresh.starter.crawler.parser.provider.XpathSelector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


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
