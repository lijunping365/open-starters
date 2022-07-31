package com.saucesubfresh.starter.lottery.component;


import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.domain.LotteryResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 默认任务执行成功处理器
 */
@Slf4j
public class DefaultLotteryExecuteSuccessHandler<Req extends LotteryRequest, Resp extends LotteryResponse> implements LotteryExecuteSuccessHandler<Req, Resp> {

  @Override
  public void onDrawExecuteSuccess(Req request, Resp response) {
    log.info("[抽奖执行成功]：{}-{}", request, response);
  }
}
