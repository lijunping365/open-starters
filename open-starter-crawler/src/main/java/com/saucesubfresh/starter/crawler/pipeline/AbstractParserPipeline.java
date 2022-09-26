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
package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.ElementSelector;
import com.saucesubfresh.starter.crawler.parser.provider.JsonPathSelector;
import com.saucesubfresh.starter.crawler.utils.ExtractorUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  抽象解析类，子类实现包括
 *  1. 动态规则解析（自动模式）
 *  2. 基于类注解解析（手动模式）
 * </pre>
 *
 * @author lijunping
 */
public abstract class AbstractParserPipeline implements ParserPipeline {

    @Override
    public void process(SpiderRequest request, SpiderResponse response) {
        Map<String, Object> parseResult = doParse(request, response);
        response.setParseResult(parseResult);
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

    protected abstract Map<String, Object> doParse(SpiderRequest request, SpiderResponse response);

}
