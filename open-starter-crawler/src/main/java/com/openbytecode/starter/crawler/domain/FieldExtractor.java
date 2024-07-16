/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.crawler.domain;

import lombok.Data;

/**
 * 抽取规则，对应新建爬虫弹窗中的 【抽取规则】
 *
 * @author lijunping
 */
@Data
public class FieldExtractor {

    /**
     * 字段名（属性名），例如：title
     */
    private String fieldName;

    /**
     * 解析方式，例如：XPath 或 JsonPath 或 Regex 或 Css
     */
    private String expressionType;

    /**
     * 解析规则，例如：//a[@class='article-link title']/span/text()
     */
    private String expressionValue;

    /**
     * 字段默认值，如果未采到使用该默认值
     */
    private String defaultValue;

    /**
     * 定义提取器是否返回多个结果，对应【数组】
     */
    private boolean multi = false;

    /**
     * 该字段是否参与唯一 id 生成，对应【主键】
     */
    private boolean unique = false;
}
