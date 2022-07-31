package com.saucesubfresh.starter.lottery.domain;

import com.saucesubfresh.starter.lottery.service.LotteryAward;
import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2021-12-28 22:38
 */
@Data
public class LotteryResponse {

    /**
     * 是否中奖
     */
    private boolean lucky;

    /**
     * 抽奖奖品
     */
    private LotteryAward award;
}