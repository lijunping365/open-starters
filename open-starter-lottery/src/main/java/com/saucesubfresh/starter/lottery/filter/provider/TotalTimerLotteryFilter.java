package com.saucesubfresh.starter.lottery.filter.provider;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.exception.LotteryException;
import com.saucesubfresh.starter.lottery.filter.Filter;

import java.time.Duration;

/**
 * 参与抽奖次数拦截器，如果已达到抽奖次数限制则直接抛出异常从而中断抽奖
 * @author lijunping on 2021/12/30
 */
@Filter(value = "totalTimerLotteryFilter", order = -100)
public class TotalTimerLotteryFilter<Req extends LotteryRequest> extends AbstractTimerLotteryFilter<Req> {

    @Override
    public void doFilter(Req request) throws LotteryException {
        boolean filterResult = executeLuaScript(
                "userRedisTotalKey",
                "1",
                "shareRespDTO.getJoinTotalLimitNum().toString()",
                String.valueOf(Duration.ofDays(1).getSeconds()));
        if (!filterResult){
            throw new LotteryException("抽奖次数已达上限");
        }
    }
}