package com.saucesubfresh.starter.cache.domain;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/22
 */
public class NullValue implements ValueWrapper, Serializable {

    private static final long serialVersionUID = -8310337775544536701L;

    public static final NullValue INSTANCE = new NullValue();

    @Override
    public Object get() {
        return null;
    }

}
