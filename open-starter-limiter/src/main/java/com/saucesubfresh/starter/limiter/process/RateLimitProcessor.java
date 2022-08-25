package com.saucesubfresh.starter.limiter.process;

import org.w3c.dom.stylesheets.LinkStyle;

/**
 * 注解限流处理器
 *
 * @author lijunping on 2022/8/25
 */
public interface RateLimitProcessor {

    boolean tryAcquire(String keys , int count);

    boolean tryAcquire(String keys ,int count,int period);
}
