package com.openbytecode.starter.crawler.test.annotation;


import com.openbytecode.starter.crawler.annotation.ExtractBy;
import com.openbytecode.starter.crawler.annotation.OpenCrawler;
import com.openbytecode.starter.crawler.domain.SpiderRequest;
import com.openbytecode.starter.crawler.enums.ExpressionType;
import com.openbytecode.starter.crawler.utils.ExtractorUtils;
import lombok.Data;

/**
 * @author lijunping
 */
@Data
@OpenCrawler(url = "http://www.baidu.com")
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
        SpiderRequest request = ExtractorUtils.getSpiderRequest(AppStore.class);
        //request.setHeaders();
        //request.setHeaders();
        //进行采集
    }

}
