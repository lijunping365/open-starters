package com.saucesubfresh.starter.lottery.domain;

import lombok.Data;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2021-12-28 22:32
 */
@Data
public class LotteryRequest {
    /**
     * 活动 id
     */
    private Long actId;
    /**
     * 用户 id
     */
    private List<Long> uidList;
}
