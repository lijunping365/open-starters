package com.saucesubfresh.starter.lottery.executor;

import com.saucesubfresh.starter.lottery.domain.RedPacketLotteryRequest;
import com.saucesubfresh.starter.lottery.domain.RedPacketLotteryResponse;

/**
 * @author lijunping on 2022/8/5
 */
public interface RedPacketLotteryExecutor extends LotteryExecutor<RedPacketLotteryRequest, RedPacketLotteryResponse>{

    RedPacketLotteryResponse doDraw(RedPacketLotteryRequest request);

}
