package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.SimpleValueWrapper;
import com.saucesubfresh.starter.cache.domain.ValueWrapper;
import org.springframework.cache.support.NullValue;
import org.springframework.lang.Nullable;

/**
 * @author lijunping on 2022/6/9
 */
public abstract class AbstractClusterCache implements ClusterCache {

    private final boolean allowNullValues;

    protected AbstractClusterCache(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    protected abstract ValueWrapper lookup(Object key);

    /**
     * 在获取时进行解析并返回
     */
    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues) {
            return null;
        }
        return storeValue;
    }

    /**
     * 缓存null对象时进行包装
     */
    protected Object toStoreValue(Object storeValue) {
        if (storeValue == null) {
            if (this.allowNullValues) {
                return NullValue.INSTANCE;
            }
            throw new IllegalArgumentException(
                    "Cache '" + getName() + "' is configured to not allow null values but null was provided");
        }
        return storeValue;
    }

    /**
     * 缓存null对象时进行包装
     */
    protected ValueWrapper toValueWrapper(Object storeValue) {
        return (storeValue != null ? new SimpleValueWrapper(fromStoreValue(storeValue)) : null);
    }


}
