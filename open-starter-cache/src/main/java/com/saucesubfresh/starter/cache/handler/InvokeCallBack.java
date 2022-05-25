package com.saucesubfresh.starter.cache.handler;

/**
 * @author: 李俊平
 * @Date: 2022-05-25 21:45
 */
@FunctionalInterface
public interface InvokeCallBack {

    Object invoke() throws Throwable;
}
