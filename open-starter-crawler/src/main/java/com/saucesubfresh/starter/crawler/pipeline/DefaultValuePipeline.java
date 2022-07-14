package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.domain.SpiderResponse;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lijunping on 2022/7/13
 */
public class DefaultValuePipeline extends AbstractValuePipeline {

    private static final String ID = "id";
    private static final String SPIDER_ID = "spiderId";
    private static final String CREATE_TIME = "createTime";

    public DefaultValuePipeline(KeyGenerator keyGenerator) {
        super(keyGenerator);
    }

    @Override
    protected void doFillValue(SpiderRequest request, SpiderResponse response) {
        List<FieldExtractor> fieldExtractors = request.getExtract();
        List<Map<String, Object>> formatResult = response.getFormatResult();
        if (CollectionUtils.isEmpty(formatResult)){
            return;
        }

        List<String> uniqueKeys = super.getUniqueKeys(fieldExtractors);
        for (Map<String, Object> rowData : formatResult) {
            long time = new Date().getTime();
            String uniqueId = super.getUniqueId(rowData, uniqueKeys, request.getSpiderId());
            rowData.putIfAbsent(ID, uniqueId);
            rowData.putIfAbsent(SPIDER_ID, request.getSpiderId());
            rowData.putIfAbsent(CREATE_TIME, time);
            fieldExtractors.stream().filter(e-> StringUtils.isNotBlank(e.getDefaultValue())).forEach(e->{
                rowData.putIfAbsent(e.getFieldName(), e.getDefaultValue());
            });
        }
    }
}
