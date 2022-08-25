package com.saucesubfresh.starter.limiter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/8/25
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.limit")
public class LimiterProperties {


}
