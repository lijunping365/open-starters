package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lijunping on 2022/4/26
 */
@Slf4j
@Component
public class CustomizeFormatPipeline extends AbstractFormatPipeline {

    @Override
    protected List<Map<String, Object>> doFormat(Map<String, Object> parseResult, List<FieldExtractor> fieldExtractors) {
        return null;
    }
}
