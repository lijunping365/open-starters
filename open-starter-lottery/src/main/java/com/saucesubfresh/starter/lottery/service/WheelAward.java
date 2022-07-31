package com.saucesubfresh.starter.lottery.service;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lijunping on 2022/1/13
 */
@Data
public class WheelAward extends LotteryAward{
    /**
     * 奖品 id
     */
    private Long awardId;

    /**
     * 奖品名称
     */
    private String awardName;

    /**
     * 中奖概率
     */
    private BigDecimal awardRate;

    /**
     * 奖品库存
     */
    private Integer awardStock;
}
