package com.saucesubfresh.starter.cache.factory;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lijunping on 2022/6/22
 */
@Slf4j
public class DefaultConfigFactory implements ConfigFactory, ResourceLoaderAware, InitializingBean {

    private ResourceLoader resourceLoader;

    protected final CacheProperties properties;

    private final ConcurrentMap<String, CacheConfig> configMap = new ConcurrentHashMap<>(16);

    public DefaultConfigFactory(CacheProperties properties) {
        this.properties = properties;
    }

    @Override
    public CacheConfig create(String cacheName) {
        CacheConfig cacheConfig = configMap.get(cacheName);
        if (Objects.nonNull(cacheConfig)){
            return cacheConfig;
        }
        CacheConfig defaultConfig = createDefault();
        configMap.put(cacheName, defaultConfig);
        return defaultConfig;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final String configLocation = properties.getConfigLocation();
        if (StringUtils.isBlank(configLocation)) {
            return;
        }

        Resource resource = resourceLoader.getResource(configLocation);
        try {
            Map<String, ? extends CacheConfig> configs = CacheConfig.fromJSON(resource.getInputStream());
            this.configMap.putAll(configs);
        } catch (IOException e) {
            try {
                Map<String, ? extends CacheConfig> configs = CacheConfig.fromYAML(resource.getInputStream());
                this.configMap.putAll(configs);
            } catch (IOException e1) {
                throw new BeanDefinitionStoreException("Could not parse cache configuration at [" + configLocation + "]", e1);
            }
        }
        log.debug("配置文件「{}」解析结果：{}", configLocation, this.configMap);
    }

    private CacheConfig createDefault(){
        return CacheConfig.builder()
                .maxIdleTime(properties.getMaxIdleTime())
                .maxSize(properties.getMaxSize())
                .ttl(properties.getTtl())
                .build();
    }

}
