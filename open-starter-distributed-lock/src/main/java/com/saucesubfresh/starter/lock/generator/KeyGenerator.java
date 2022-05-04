package com.saucesubfresh.starter.lock.generator;

import java.lang.reflect.Method;

/**
 * @author lijunping on 2021/12/30
 */
public interface KeyGenerator {
    /**
     * 生成 key
     * @param method
     * @param args
     * @return
     */
    String generate(Method method, Object[] args);
}
