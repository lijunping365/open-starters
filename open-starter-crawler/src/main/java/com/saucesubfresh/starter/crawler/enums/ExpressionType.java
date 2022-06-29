package com.saucesubfresh.starter.crawler.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ExpressionType {

    XPath, Regex, Css, JsonPath;

    public static ExpressionType of(String expressionType){
        return Arrays.stream(ExpressionType.values()).filter(type -> StringUtils.equals(expressionType, type.name())).findFirst().orElse(null);
    }

}
