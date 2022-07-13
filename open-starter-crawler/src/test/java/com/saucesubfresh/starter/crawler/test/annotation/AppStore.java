package com.saucesubfresh.starter.crawler.test.annotation;


import com.saucesubfresh.starter.crawler.annotation.ExtractBy;
import com.saucesubfresh.starter.crawler.domain.FieldExtractor;
import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.enums.ExpressionType;
import com.saucesubfresh.starter.crawler.parser.ExtractorUtils;
import lombok.Data;

import java.util.List;

/**
 * @author code4crafter@gmail.com
 * @since 0.4.1
 */
@Data
public class AppStore {

    @ExtractBy(type = ExpressionType.JsonPath, value = "$..trackName")
    private String trackName;

    @ExtractBy(type = ExpressionType.JsonPath, value = "$..description")
    private String description;

    @ExtractBy(type = ExpressionType.JsonPath, value = "$..userRatingCount")
    private String userRatingCount;

    @ExtractBy(type = ExpressionType.JsonPath, value = "$..screenshotUrls")
    private String screenshotUrls;

    @ExtractBy(type = ExpressionType.JsonPath, value = "$..supportedDevices")
    private String supportedDevices;

    public void extractorRuleFromClass(){
        List<FieldExtractor> fieldExtractors = ExtractorUtils.getFieldExtractors(AppStore.class);
        // 进行采集
        SpiderRequest request = new SpiderRequest();
        request.setExtract(fieldExtractors);
    }

}
