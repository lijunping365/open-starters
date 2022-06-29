package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.utils.ExtractorUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 基于类注解解析（手动模式）
 * @author lijunping on 2022/4/15
 */
@Component
public class CustomizeParserPipeline extends AbstractParserPipeline {

    @Override
    protected Map<String, Object> doParse(String body, List<FieldExtractor> fieldExtractors) {
        return null;
    }

    @Override
    protected List<FieldExtractor> getFieldExtractors(SpiderRequest request) {
        return ExtractorUtils.getFieldExtractors(Object.class);
    }
}
