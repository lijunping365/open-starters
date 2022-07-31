package com.saucesubfresh.starter.lottery.filter;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.exception.LotteryException;

/**
 * @author lijunping on 2021/12/27
 */
public interface LotteryFilter {

    /**
     * 执行过滤
     *
     * @param request
     * @throws LotteryException
     */
    void doFilter(LotteryRequest request) throws LotteryException;
}
