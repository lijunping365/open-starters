package com.saucesubfresh.starter.lottery.filter.provider;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.exception.LotteryException;
import com.saucesubfresh.starter.lottery.filter.Filter;
import com.saucesubfresh.starter.lottery.filter.LotteryFilter;
import org.springframework.stereotype.Component;

/**
 * 参与活动抽奖时间校验器
 * 如果在活动期间内，返回 true
 * 如果不在活动期间内，返回 false
 * @author lijunping on 2022/1/4
 */
@Filter(value = "actTimeFilter", order = 1)
public class ActTimeFilter<Req extends LotteryRequest> implements LotteryFilter<Req> {

    @Override
    public void doFilter(Req request) throws LotteryException {

    }
}
