package com.saucesubfresh.starter.cache.manager;

import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.properties.CacheConfig;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:57
 */
@Slf4j
public abstract class AbstractCacheManager implements CacheManager, ApplicationContextAware, SmartInitializingSingleton {

    private final ConcurrentMap<String, ClusterCache> cacheMap = new ConcurrentHashMap<>(16);

    private final ConcurrentMap<String, CacheConfig> configMap = new ConcurrentHashMap<>(16);

    private ApplicationContext applicationContext;

    protected final CacheProperties properties;

    protected AbstractCacheManager(CacheProperties properties) {
        this.properties = properties;
    }

    @Override
    public ClusterCache getCache(String cacheName) {
        ClusterCache cache = cacheMap.get(cacheName);
        if (Objects.nonNull(cache)){
            return cache;
        }
        CacheConfig cacheConfig = configMap.get(cacheName);
        cache = this.createCache(cacheName, cacheConfig);
        ClusterCache oldCache = cacheMap.putIfAbsent(cacheName, cache);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Map<String, ClusterCache> getCacheList() {
        return cacheMap;
    }

    @Override
    public Map<String, CacheConfig> getCacheConfigList() {
        return configMap;
    }

    @Override
    public void afterSingletonsInstantiated() {
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            Map<Method, OpenCacheable> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<OpenCacheable>() {
                            @Override
                            public OpenCacheable inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, OpenCacheable.class);
                            }
                        });
            } catch (Throwable ex) {
                log.error("OpenCacheable resolve error for bean[" + beanDefinitionName + "].", ex);
            }
            if (annotatedMethods == null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, OpenCacheable> methodOpenCacheableEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodOpenCacheableEntry.getKey();
                OpenCacheable annotation = methodOpenCacheableEntry.getValue();
                buildCacheConfig(annotation, bean, executeMethod);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void buildCacheConfig(OpenCacheable openCacheable, Object bean, Method executeMethod){
        if (openCacheable == null) {
            return;
        }

        String name = openCacheable.value();
        Class<?> clazz = bean.getClass();
        String methodName = executeMethod.getName();
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("open-cache method-OpenCacheable name invalid, for[" + clazz + "#" + methodName + "] .");
        }
        if (configMap.get(name) != null) {
            throw new RuntimeException("open-cache OpenCacheable[" + name + "] naming conflicts.");
        }

        String cacheName = openCacheable.cacheName();
        CacheConfig cacheConfig = CacheConfig.builder()
                        .cacheName(openCacheable.cacheName())
                        .maxSize(properties.getMaxSize())
                        .ttl(properties.getTtl())
                        .build();
        configMap.put(cacheName, cacheConfig);
    }

    protected abstract ClusterCache createCache(String cacheName, CacheConfig cacheConfig);
}
