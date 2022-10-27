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
package com.saucesubfresh.starter.crawler.handler;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.ElementSelector;
import com.saucesubfresh.starter.crawler.parser.provider.JsonPathSelector;
import com.saucesubfresh.starter.crawler.utils.ExtractorUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;

/**
 * <pre>
 *  抽象结果处理类，子类实现包括
 * </pre>
 *
 * @author lijunping
 */
public abstract class AbstractResultSetHandler implements ResultSetHandler {

    @Override
    public List<Map<String, Object>> handler(SpiderRequest request, String content) {
        return doHandler(request, content);
    }

    /**
     * 解析 json
     *
     * @param json
     * @param fieldExtractors
     * @return
     */
    protected Map<String, Object> parseJson(String json, final List<FieldExtractor> fieldExtractors){
        Map<String, Object> fields = new HashMap<>();
        JsonPathSelector jsonPathSelector = new JsonPathSelector(json);
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
        return fields;
    }

    /**
     * 解析网页
     *
     * @param html
     * @param fieldExtractors
     * @return
     */
    protected Map<String, Object> parseHtml(String html, final List<FieldExtractor> fieldExtractors){
        Map<String, Object> fields = new HashMap<>();
        Document document = Jsoup.parse(html);
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
        return fields;
    }

    /**
     * 解析结果格式化
     *
     * @param fields
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> formatList(Map<String, Object> fields){
        List<Map<String, Object>> formatResult = new ArrayList<>();
        Map<String, List<String>> dataTmp = new HashMap<>();
        fields.forEach((key, value)-> dataTmp.put(key, (List<String>) value));
        int maxSize = dataTmp.values().stream().map(List::size).max(Comparator.comparing(Integer::intValue)).orElse(0);

        for (int i = 0; i <maxSize; i++) {
            Map<String, Object> rowData = new HashMap<>();
            int finalI = i;
            dataTmp.forEach((key, value)-> rowData.put(key, finalI >= value.size() ? null : value.get(finalI)));
            formatResult.add(rowData);
        }
        return formatResult;
    }

    protected abstract List<Map<String, Object>> doHandler(SpiderRequest request, String content);

}
