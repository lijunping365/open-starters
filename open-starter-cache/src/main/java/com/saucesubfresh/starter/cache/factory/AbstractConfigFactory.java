package com.saucesubfresh.starter.cache.factory;

import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 *     1. 项目启动会自动加载缓存配置文件并放到 configMap 中
 *     2. 在 configMap 中未找到配置文件时会创建默认的 CacheConfig 并返回
 * </p>
 * @author lijunping on 2022/6/22
 */
@Slf4j
public abstract class AbstractConfigFactory implements ConfigFactory, InitializingBean{

    protected final CacheProperties properties;

    private final ConcurrentMap<String, CacheConfig> configMap = new ConcurrentHashMap<>(16);

    public AbstractConfigFactory(CacheProperties properties) {
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
    public void afterPropertiesSet() throws Exception {
        Map<String, ? extends CacheConfig> config = this.loadConfig();
        if (CollectionUtils.isEmpty(config)){
            configMap.putAll(config);
        }
    }

    private CacheConfig createDefault(){
        return CacheConfig.builder()
                .maxIdleTime(properties.getMaxIdleTime())
                .maxSize(properties.getMaxSize())
                .ttl(properties.getTtl())
                .build();
    }

    protected abstract Map<String, ? extends CacheConfig> loadConfig();

}
