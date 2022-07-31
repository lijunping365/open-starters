package com.saucesubfresh.starter.lottery.event;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import lombok.Data;

/**
 * @author lijunping on 2021/12/9
 */
@Data
public class LotteryEventInformation {

    private LotteryRequest lotteryRequest;

    private LotteryResponse lotteryResponse;
}
