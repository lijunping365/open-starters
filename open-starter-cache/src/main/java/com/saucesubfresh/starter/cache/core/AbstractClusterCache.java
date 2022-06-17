package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.SimpleValueWrapper;
import com.saucesubfresh.starter.cache.domain.ValueWrapper;

/**
 * @author lijunping on 2022/6/9
 */
public abstract class AbstractClusterCache implements ClusterCache {

    /**
     * 在取出缓存时调用
     */
    protected Object getWrapperValue(ValueWrapper valueWrapper) {
        return valueWrapper.get();
    }

    /**
     * 在进行缓存时调用，如果缓存值为 null对象则进行包装
     */
    protected ValueWrapper wrapper(Object value) {
        return new SimpleValueWrapper(value);
    }

}
