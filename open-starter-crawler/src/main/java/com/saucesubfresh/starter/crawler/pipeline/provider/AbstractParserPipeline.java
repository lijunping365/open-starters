package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.ElementSelector;
import com.saucesubfresh.starter.crawler.parser.provider.JsonPathSelector;
import com.saucesubfresh.starter.crawler.pipeline.Pipeline;
import com.saucesubfresh.starter.crawler.utils.ExtractorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象解析类，子类实现包括
 * 1. 动态规则解析（自动模式）
 * 2. 基于类注解解析（手动模式）
 * @author: 李俊平
 * @Date: 2022-04-19 22:49
 */
public abstract class AbstractParserPipeline implements Pipeline {

    @Override
    public void handler(SpiderRequest request, SpiderResponse response) {
        String body = response.getBody();
        final List<FieldExtractor> fieldExtractors = getFieldExtractors(request);
        if (StringUtils.isBlank(body) || CollectionUtils.isEmpty(fieldExtractors)){
            return;
        }
        response.setFieldExtractors(fieldExtractors);
        Map<String, Object> parseResult = doParse(body, fieldExtractors);
        response.setParseResult(parseResult);
    }

    protected Map<String, Object> parseJson(String json, final List<FieldExtractor> fieldExtractors){
        Map<String, Object> fields = new HashMap<>();
        JsonPathSelector jsonPathSelector = new JsonPathSelector(json);
        for (FieldExtractor extractor : fieldExtractors) {
            if (extractor.isMulti()){
                List<String> results = jsonPathSelector.selectList(extractor.getExpressionValue());
                fields.put(extractor.getFieldName(), results);
            }else {
                String result = jsonPathSelector.select(extractor.getExpressionValue());
                fields.put(extractor.getFieldName(), result);
            }
        }
        return fields;
    }

    protected Map<String, Object> parseHtml(String html, final List<FieldExtractor> fieldExtractors){
        Map<String, Object> fields = new HashMap<>();
        Document document = Jsoup.parse(html);
        for (FieldExtractor extractor : fieldExtractors) {
            ExpressionType type = ExpressionType.of(extractor.getExpressionType());
            ElementSelector selector = (ElementSelector) ExtractorUtils.getSelector(type, extractor.getExpressionValue());
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

    protected abstract Map<String, Object> doParse(String body, final List<FieldExtractor> fieldExtractors);

    protected abstract List<FieldExtractor> getFieldExtractors(SpiderRequest request);
}
