package com.saucesubfresh.starter.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lijunping on 2022/5/20
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.cache")
public class CacheProperties {

    /**
     * 命名空间，相当于应用名称
     */
    private String namespace;

    /**
     * 是否开启统计功能，默认开启
     */
    private boolean statistical = true;
}
