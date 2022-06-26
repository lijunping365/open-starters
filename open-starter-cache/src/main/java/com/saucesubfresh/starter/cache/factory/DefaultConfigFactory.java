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
 * <p>
 *     加载配置文件的默认实现，默认加载 yaml 文件
 * </p>
 *
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
    protected Map<String, ? extends CacheConfig> loadConfig() {
        final String configLocation = properties.getConfigLocation();
        if (StringUtils.isBlank(configLocation)) {
            return Collections.emptyMap();
        }

        try {
            Resource resource = resourceLoader.getResource(configLocation);
            return CacheConfig.fromYAML(resource.getInputStream());
        } catch (IOException e) {
            log.warn("加载配置文件：{}，异常 {}", properties.getConfigLocation(), e.getMessage());
            return Collections.emptyMap();
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
