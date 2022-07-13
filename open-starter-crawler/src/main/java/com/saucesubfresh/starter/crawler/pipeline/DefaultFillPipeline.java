package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/7/13
 */
public class DefaultFillPipeline extends AbstractFillPipeline{

    private static final String ID = "id";
    private static final String CREATE_TIME = "createTime";

    public DefaultFillPipeline(KeyGenerator keyGenerator) {
        super(keyGenerator);
    }

    @Override
    protected void doFill(SpiderRequest request, SpiderResponse response) {
        List<FieldExtractor> fieldExtractors = request.getExtract();
        List<Map<String, Object>> formatResult = response.getFormatResult();
        if (CollectionUtils.isEmpty(formatResult)){
            return;
        }

        List<String> uniqueKeys = fieldExtractors.stream()
                .filter(FieldExtractor::isUnique)
                .map(FieldExtractor::getFieldName)
                .collect(Collectors.toList());

        // 如果未指定唯一 id，将使用第一个字段为唯一 id
        if (CollectionUtils.isEmpty(uniqueKeys)){
            uniqueKeys = Collections.singletonList(fieldExtractors.get(0).getFieldName());
        }

        for (Map<String, Object> rowData : formatResult) {
            long time = new Date().getTime();
            String uniqueId = super.getUniqueId(rowData, uniqueKeys, request.getSpiderId());
            rowData.putIfAbsent(ID, uniqueId);
            rowData.putIfAbsent(CREATE_TIME, time);
        }
    }
}
