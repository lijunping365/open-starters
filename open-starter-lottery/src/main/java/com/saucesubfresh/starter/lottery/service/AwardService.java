package com.saucesubfresh.starter.lottery.service;

import java.util.List;
import java.util.Optional;

/**
 * @author: 李俊平
 * @Date: 2021-12-28 23:10
 */
public interface AwardService {

    /**
     * 根据活动id获取奖品列表
     * @param activityId 活动 id
     * @return 奖品列表
     */
    <T extends LotteryAward> Optional<List<T>> getAwardList(Long activityId);

}
