package com.saucesubfresh.starter.crawler.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/7/13
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.crawler")
public class CrawlerProperties {


}
