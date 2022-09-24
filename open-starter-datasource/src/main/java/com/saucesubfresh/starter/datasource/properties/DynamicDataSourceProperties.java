package com.saucesubfresh.starter.datasource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/9/24
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.datasource")
public class DynamicDataSourceProperties {
}
