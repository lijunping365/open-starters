package com.saucesubfresh.starter.crawler.pipeline.provider;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认数据格式化
 * @author lijunping on 2022/4/26
 */
@Slf4j
@Component
public class DefaultFormatPipeline extends AbstractFormatPipeline {

    @Override
    protected List<Map<String, Object>> doFormat(Map<String, Object> parseResult, List<FieldExtractor> fieldExtractors) {
        List<String> uniqueKeys = fieldExtractors.stream().filter(FieldExtractor::isUnique).map(FieldExtractor::getFieldName).collect(Collectors.toList());
        final long count = fieldExtractors.stream().filter(FieldExtractor::isMulti).count();
        // 如果未指定唯一 id，将使用第一个字段为唯一 id
        if (CollectionUtils.isEmpty(uniqueKeys)){
            uniqueKeys = Collections.singletonList(parseResult.keySet().iterator().next());
        }
        List<Map<String, Object>> formatResult;
        // 如果所有字段的值解析后都是 List，则转换成 List
        if (fieldExtractors.size() == count){
            formatResult = formatList(parseResult, uniqueKeys);
        }else {
            formatResult = formatObject(parseResult, uniqueKeys);
        }
        return formatResult;
    }


}
