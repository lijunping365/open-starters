package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import com.saucesubfresh.starter.lottery.exception.LotteryException;

/**
 * 抽奖入口
 *
 * @author lijunping on 2021/12/28
 */
public interface LotteryExecutor<Q extends LotteryRequest, R extends LotteryResponse> {

    /**
     * 抽奖入口，抽奖完成后返回抽奖结果
     *
     * @param request
     * @return
     * @throws LotteryException
     */
    R doDraw(Q request) throws LotteryException;
}
