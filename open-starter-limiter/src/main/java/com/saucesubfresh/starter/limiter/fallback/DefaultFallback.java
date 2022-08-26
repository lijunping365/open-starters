package com.saucesubfresh.starter.limiter.fallback;

/**
 * @author lijunping on 2022/8/26
 */
public class DefaultFallback<T> implements Fallback<T>{

    @Override
    public T fallback() {
        return null;
    }
}
