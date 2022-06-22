package com.saucesubfresh.starter.cache.core;

import com.saucesubfresh.starter.cache.domain.NullValue;

/**
 * @author lijunping on 2022/6/9
 */
public abstract class AbstractClusterCache implements ClusterCache {

    protected Object toValueWrapper(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().getName().equals(NullValue.class.getName())) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    protected Object toStoreValue(Object userValue) {
        if (userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }
}
