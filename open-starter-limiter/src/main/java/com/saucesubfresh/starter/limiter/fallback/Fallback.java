package com.saucesubfresh.starter.limiter.fallback;

/**
 * @author lijunping on 2022/8/26
 */
@FunctionalInterface
public interface Fallback<T> {

    T fallback();
}
