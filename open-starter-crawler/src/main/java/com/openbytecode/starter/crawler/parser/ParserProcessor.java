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
package com.openbytecode.starter.crawler.parser;

import com.openbytecode.starter.crawler.domain.FieldExtractor;
import com.openbytecode.starter.crawler.enums.ExpressionType;
import com.openbytecode.starter.crawler.parser.provider.JsonPathSelector;
import com.openbytecode.starter.crawler.utils.ExtractorUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijunping on 2022-10-29 18:25
 */
public class ParserProcessor {

    /**
     * 解析
     *
     * @param body
     * @param fieldExtractors
     * @return
     */
    public static Map<String, Object> processor(ExpressionType parseType, String body, final List<FieldExtractor> fieldExtractors){
        Map<String, Object> fields = new HashMap<>();
        if (parseType == ExpressionType.JsonPath){
            JsonPathSelector jsonPathSelector = new JsonPathSelector(body);
            for (FieldExtractor extractor : fieldExtractors) {
                String expressionValue = extractor.getExpressionValue();
                if (extractor.isMulti()){
                    List<String> results = jsonPathSelector.selectList(expressionValue);
                    fields.put(extractor.getFieldName(), results);
                }else {
                    String result = jsonPathSelector.select(expressionValue);
                    fields.put(extractor.getFieldName(), result);
                }
            }
        } else {
            Document document = Jsoup.parse(body);
            for (FieldExtractor extractor : fieldExtractors) {
                String expression = extractor.getExpressionValue();
                ExpressionType type = ExpressionType.of(extractor.getExpressionType());
                ElementSelector selector = (ElementSelector) ExtractorUtils.getSelector(type, expression);
                if (extractor.isMulti()){
                    List<String> results = selector.selectList(document);
                    fields.put(extractor.getFieldName(), results);
                }else {
                    String result = selector.select(document);
                    fields.put(extractor.getFieldName(), result);
                }
            }
        }

        return fields;
    }
}
