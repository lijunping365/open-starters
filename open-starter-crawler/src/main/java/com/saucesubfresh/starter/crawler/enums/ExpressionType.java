package com.saucesubfresh.starter.crawler.enums;

import com.saucesubfresh.starter.crawler.exception.CrawlerException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ExpressionType {

    XPath, Regex, Css, JsonPath;

    public static ExpressionType of(String expressionType){
        return Arrays.stream(ExpressionType.values())
                .filter(type -> StringUtils.equals(expressionType, type.name()))
                .findFirst().orElseThrow(()->new CrawlerException("不支持该操作：" + expressionType));
    }

}
