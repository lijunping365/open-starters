package com.saucesubfresh.starter.cache.factory;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author: 李俊平
 * @Date: 2022-06-22 21:31
 */
@Slf4j
public class DefaultConfigFactory extends AbstractConfigFactory implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public DefaultConfigFactory(CacheProperties properties) {
        super(properties);
    }

    @Override
    protected Map<String, ? extends CacheConfig> initConfig() {
        final String configLocation = properties.getConfigLocation();
        if (StringUtils.isBlank(configLocation)) {
            return Collections.emptyMap();
        }

        Resource resource = resourceLoader.getResource(configLocation);
        try {
            return CacheConfig.fromYAML(resource.getInputStream());
        } catch (IOException e) {
            log.warn("加载配置文件：{}，异常 {}", properties.getConfigLocation(), e);
            return Collections.emptyMap();
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
