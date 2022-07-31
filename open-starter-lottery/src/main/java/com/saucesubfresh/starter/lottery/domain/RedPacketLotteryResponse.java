package com.saucesubfresh.starter.lottery.domain;

import lombok.Data;

/**
 * 手气红包
 * @author lijunping on 2022/1/7
 */
@Data
public class RedPacketLotteryResponse extends LotteryResponse{

    /**
     * 手气红包
     */
    private Long luckyAmount;
}
