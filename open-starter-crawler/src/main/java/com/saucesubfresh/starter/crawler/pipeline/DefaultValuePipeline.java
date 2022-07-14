package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.generator.KeyGenerator;

import java.util.Map;

/**
 * @author lijunping on 2022/7/13
 */
public class DefaultValuePipeline extends AbstractValuePipeline {

    public DefaultValuePipeline(KeyGenerator keyGenerator) {
        super(keyGenerator);
    }

    @Override
    protected void doFillValue(SpiderRequest request, Map<String, Object> rowData) {

    }
}
