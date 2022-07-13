package com.saucesubfresh.starter.crawler.pipeline;


import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 动态规则解析（自动模式）
 * @author lijunping on 2022/4/15
 */
public class DefaultParserPipeline extends AbstractParserPipeline {

    @Override
    protected Map<String, Object> doParse(SpiderRequest request, SpiderResponse response) {
        String body = response.getBody();
        final List<FieldExtractor> fieldExtractors = request.getExtract();
        if (StringUtils.isBlank(body) || CollectionUtils.isEmpty(fieldExtractors)){
            return null;
        }
        Map<String, Object> dataMap;
        ExpressionType type = ExpressionType.of(fieldExtractors.get(0).getExpressionType());
        if (type == ExpressionType.JsonPath){
            dataMap = parseJson(body, fieldExtractors);
        } else {
            dataMap = parseHtml(body, fieldExtractors);
        }
        return dataMap;
    }
}
