package com.saucesubfresh.starter.lottery.service;

import lombok.Data;

/**
 * @author lijunping on 2022/1/13
 */
@Data
public class RedPacketAward extends LotteryAward {
    /**
     * 红包最小金额，单位为 “分“
     */
    private Long minAmount = 1L;

    /**
     * 红包总金额，单位为 “分“
     */
    private Long packageAmount;

    /**
     * 红包个数
     */
    private Integer packageSize;
}
