package com.saucesubfresh.starter.lottery.service;

import java.util.List;

/**
 * @author lijunping on 2021/12/30
 */
public interface LotteryService {

    /**
     * 初始化活动规则
     * @param actId
     * @return
     */
    List<String> getActFilterChain(Long actId);

    /**
     * 增加用户参与活动的抽奖次数
     * @param uid
     * @param actId
     * @return
     */
    boolean increaseTimes(Long uid, Long actId);
}
