package com.saucesubfresh.starter.lottery.initializer;

/**
 * 抽奖环境初始化器
 * @author lijunping on 2022/1/7
 */
public interface LotteryInitializer {
    /**
     * 初始化抽奖环境
     */
    void init(Long actId);
}
