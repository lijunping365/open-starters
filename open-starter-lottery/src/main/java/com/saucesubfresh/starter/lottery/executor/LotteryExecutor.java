package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import com.saucesubfresh.starter.lottery.exception.LotteryException;

/**
 * 抽奖入口
 *
 * @author lijunping on 2021/12/28
 */
public interface LotteryExecutor<Req extends LotteryRequest, Resp extends LotteryResponse> {

    /**
     * 同步抽奖，抽奖完成后返回抽奖结果
     *
     * @param request
     * @return
     * @throws LotteryException
     */
    Resp doDraw(Req request) throws LotteryException;

    /**
     * 异步抽奖，抽奖完成后返回抽奖结果
     *
     * @param request
     * @throws LotteryException
     */
    void doDrawAsync(Req request) throws LotteryException;
}
