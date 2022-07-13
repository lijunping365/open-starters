package com.saucesubfresh.starter.crawler.domain;

import lombok.Data;

/**
 * 抽取规则，对应新建爬虫弹窗中的 【抽取规则】
 * @author lijunping on 2022/4/18
 */
@Data
public class FieldExtractor {

    /**
     * 解析方式，例如：XPath 或 JsonPath 或 Regex 或 Css
     */
    private String expressionType;

    /**
     * 解析规则，例如：//a[@class='article-link title']/span/text()
     */
    private String expressionValue;

    /**
     * 字段名（属性名），例如：title
     */
    private String fieldName;

    /**
     * 字段默认值，如果未采到使用该默认值
     */
    private String defaultValue;

    /**
     * 定义提取器是否返回多个结果，对应【循环】
     */
    private boolean multi = false;

    /**
     * 该字段是否参与唯一 id 生成，对应【主键】
     */
    private boolean unique = false;
}
