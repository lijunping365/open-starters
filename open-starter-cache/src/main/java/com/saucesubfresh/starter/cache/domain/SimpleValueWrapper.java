package com.saucesubfresh.starter.cache.domain;

/**
 * @author lijunping on 2022/6/9
 */
public class SimpleValueWrapper implements ValueWrapper {

    private final Object value;

    public SimpleValueWrapper(Object value) {
        this.value = value;
    }
    @Override
    public Object get() {
        return this.value;
    }
}
