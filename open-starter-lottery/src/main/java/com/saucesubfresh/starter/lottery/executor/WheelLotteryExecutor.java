package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.domain.WheelLotteryRequest;
import com.saucesubfresh.starter.lottery.domain.WheelLotteryResponse;

/**
 * @author lijunping on 2022/8/5
 */
public interface WheelLotteryExecutor extends LotteryExecutor<WheelLotteryRequest, WheelLotteryResponse>{

    WheelLotteryResponse doDraw(WheelLotteryRequest request);

}
