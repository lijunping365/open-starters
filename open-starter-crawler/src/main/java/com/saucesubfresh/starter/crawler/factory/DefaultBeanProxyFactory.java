/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.crawler.factory;

import com.saucesubfresh.starter.crawler.plugin.InterceptorChain;
import com.saucesubfresh.starter.crawler.plugin.UsePlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扫描所有带有 @UsePlugin 注解的类都放到 proxyMap 中
 *
 * @author lijunping on 2022/10/27
 */
@Slf4j
public class DefaultBeanProxyFactory implements BeanProxyFactory, SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Map<Class<?>, Object> proxyMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getProxyBean(Class<T> type){
        return (T) proxyMap.get(type);
    }

    @Override
    public void afterSingletonsInstantiated() {
        InterceptorChain interceptorChain = applicationContext.getBean(InterceptorChain.class);
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(UsePlugin.class);
        if (ObjectUtils.isEmpty(beans)){
            log.warn("Could not find a class marked with an annotation UsePlugin.");
            return;
        }
        beans.forEach((k,v)->{
            UsePlugin annotation = v.getClass().getAnnotation(UsePlugin.class);
            proxyMap.put(annotation.type(), interceptorChain.pluginAll(v));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
