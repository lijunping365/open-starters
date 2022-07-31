package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import com.saucesubfresh.starter.lottery.exception.LotteryException;

/**
 * @author lijunping on 2021/12/28
 */
public interface LotteryExecutor<Req extends LotteryRequest, Resp extends LotteryResponse> {

    Resp doDraw(Req request) throws LotteryException;
}
