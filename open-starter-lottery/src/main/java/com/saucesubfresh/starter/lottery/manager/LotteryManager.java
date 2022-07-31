package com.saucesubfresh.starter.lottery.manager;

import com.saucesubfresh.starter.lottery.initializer.LotteryInitializer;

import java.util.List;


/**
 * @author lijunping on 2022/1/13
 */
public interface LotteryManager extends LotteryInitializer {

    /**
     * 查询奖品库存
     * @param actId 活动 id
     * @param <T>
     * @return
     */
    <T extends AwardStock> List<T> getAwardStock(Long actId);
}
